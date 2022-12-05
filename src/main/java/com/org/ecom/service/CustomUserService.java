package com.org.ecom.service;

import com.org.ecom.dto.security.CustomJWTRequestDto;
import com.org.ecom.dto.security.CustomJWTResponseDto;
import com.org.ecom.dto.security.CustomMailRequestDto;
import com.org.ecom.dto.security.CustomResetPasswordDto;
import com.org.ecom.entity.registration.Role;
import com.org.ecom.entity.registration.UserEntity;
import com.org.ecom.entity.security.AccessTokens;
import com.org.ecom.entity.security.RefreshTokens;
import com.org.ecom.entity.security.ResetToken;
import com.org.ecom.exception.InvalidTokenException;
import com.org.ecom.exception.InvalidUsernameOrPasswordException;
import com.org.ecom.exception.PasswordExpiredException;
import com.org.ecom.exception.TokenExpiredException;
import com.org.ecom.repository.registration.RoleRepository;
import com.org.ecom.repository.registration.UserRepository;
import com.org.ecom.security.*;
import com.org.ecom.utility.CustomJwtUtility;
import com.org.ecom.utility.ObjectConverter;
import com.org.ecom.validation.ForgotPasswordValidations;
import com.org.ecom.validation.LoginLogoutValidations;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.rmi.activation.ActivationException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CustomUserService implements UserDetailsService {

    @Autowired
    private CustomJwtUtility jwtUtility;

    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    private UserRepository customUsersRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomizedAuditorAware customizedAuditorAware;

    @Autowired
    private ObjectConverter objectConverter;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;

    @Autowired
    private LoginLogoutValidations validations;

    @Autowired
    private ForgotPasswordValidations forgotPasswordValidations;

    @Autowired
    private AccessTokensService accessTokensService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private ResetTokenService resetTokenService;

    @Autowired
    private ActivationTokenService activationTokenService;

    @Autowired
    private MessageSource messageSource;

    Logger logger = LoggerFactory.getLogger(CustomUserService.class);

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserEntity byUsername = customUsersRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(messageSource.getMessage("message58.txt", null, LocaleContextHolder.getLocale())));

        return new User(byUsername.getUsername(), byUsername.getPassword(),
                mapRolesToAuthorities(byUsername.getRoles()));
    }

    private Collection<GrantedAuthority> mapRolesToAuthorities(Set<Role> rolesSet) {
        return rolesSet.stream().map(role -> new SimpleGrantedAuthority(role.getAuthority())).collect(Collectors.toSet());
    }

    public UserEntity findUserByUsernameAndEmail(String username, String email) {
        UserEntity findByUsernameAndEmail = customUsersRepository.findByUsernameAndEmail(username, email)
                .orElseThrow(() -> new UsernameNotFoundException(messageSource.getMessage("message59.txt", null, LocaleContextHolder.getLocale())));

        return findByUsernameAndEmail;
    }

    public ResponseEntity<String> resetPassword(CustomResetPasswordDto customResetPassword) {

        CustomResetPasswordDto resetPassword = forgotPasswordValidations.resetValidations(customResetPassword);
        String jwtToken = resetPassword.getJwtToken();

        String username = jwtUtility.extractUsername(jwtToken);

        UserEntity byUsername = customUsersRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(messageSource.getMessage("message58.txt", null, LocaleContextHolder.getLocale())));

        String encode = passwordEncoder.encode(customResetPassword.getPassword());
        byUsername.setPassword(encode);
        byUsername.setIsExpired(false);

        customizedAuditorAware.setName(byUsername.getFirstName());

        byUsername.setPasswordUpdateDate(new Date());

        customUsersRepository.save(byUsername);

        emailSenderService.sendEmail(byUsername.getEmail(),
                messageSource.getMessage("message60.txt", null, LocaleContextHolder.getLocale()),
                messageSource.getMessage("message61.txt", null, LocaleContextHolder.getLocale()));

        resetTokenService.deleteToken(byUsername.getUserId(), jwtToken);
        return ResponseEntity.ok(messageSource.getMessage("message61.txt", null, LocaleContextHolder.getLocale()));
    }


    public ResponseEntity<String> forgotPassword(CustomMailRequestDto customMailRequest) throws ActivationException {

        if (forgotPasswordValidations.forget_password_validation(customMailRequest)) {
            UserEntity user = findUserByUsernameAndEmail(customMailRequest.getUsername(), customMailRequest.getEmail());

            String resetToken =
                    this.jwtUtility.generateResetToken(user);

            ResetToken resetToken1 = new ResetToken();
            resetToken1.setUser(user);
            resetToken1.setToken(resetToken);

            resetTokenService.saveData(resetToken1);

            emailSenderService.sendEmail(user.getEmail(),
                    messageSource.getMessage("message62.txt", null, LocaleContextHolder.getLocale()),
                    String.valueOf(new CustomJWTResponseDto(resetToken).getJwtToken()));

            return ResponseEntity.ok(messageSource.getMessage("message63.txt", null, LocaleContextHolder.getLocale()));
        } else {
            ResponseEntity.BodyBuilder status = ResponseEntity.status(HttpStatus.BAD_REQUEST);
            return (ResponseEntity<String>) status;
        }
    }


    public ResponseEntity<String> logout(CustomJWTResponseDto customJWTResponse) {

        String jwtToken = customJWTResponse.getJwtToken();

        UserEntity user = userRepository.findByUsername(jwtUtility.extractUsername(jwtToken)).get();

        if (accessTokensService.accessTokens(jwtToken, user.getUserId()).isPresent()) {
            if (jwtUtility.extractExpiration(jwtToken).after(new Date())) {

                accessTokensService.logout(jwtToken);
                return ResponseEntity.ok(messageSource.getMessage("message64.txt", null, LocaleContextHolder.getLocale()));
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }

    public ResponseEntity<CustomJWTResponseDto> generate_access_token(CustomJWTResponseDto customJWTResponse) {
        String jwtToken = customJWTResponse.getJwtToken();

        UserEntity user = userRepository.findByUsername(jwtUtility.extractUsername(jwtToken)).get();

        if (refreshTokenService.refreshTokens(jwtToken, user.getUserId()).isPresent()) {
            if (jwtUtility.extractExpiration(jwtToken).after(new Date(System.currentTimeMillis()))) {
                String username = jwtUtility.extractUsername(jwtToken);

                UserDetails userDetails = loadUserByUsername(username);

                String access_token =
                        this.jwtUtility.generateAccessToken(userDetails);

                AccessTokens accessTokens = new AccessTokens();
                accessTokens.setUser(user);
                accessTokens.setToken(access_token);
                accessTokensService.saveData(accessTokens);

                return ResponseEntity.ok(new CustomJWTResponseDto(access_token));
            } else {

                throw new TokenExpiredException(messageSource.getMessage("message65.txt", null, LocaleContextHolder.getLocale()));
            }
        } else {
            throw new InvalidTokenException(messageSource.getMessage("message66.txt", null, LocaleContextHolder.getLocale()));
        }
    }

    Optional<UserEntity> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public ResponseEntity<Map<String, CustomJWTResponseDto>> login(CustomJWTRequestDto jwtRequest) throws ActivationException {

        Map<String, CustomJWTResponseDto> map = new Hashtable();

        if (validations.login_validation(jwtRequest)) {

            UserDetails userDetails = loadUserByUsername(jwtRequest.getUsername());

            UserEntity byUsername = customUsersRepository.findByUsername(userDetails.getUsername()).get();

            Date date = new Date();

            if (byUsername.getPasswordUpdateDate().getTime() - date.getTime() >= 30) {
                userRepository.updateByIsExpired(byUsername.getEmail(), true);

                emailSenderService.sendEmail(byUsername.getEmail(),
                        messageSource.getMessage("message68.txt", null, LocaleContextHolder.getLocale()),
                        messageSource.getMessage("message69.txt", null, LocaleContextHolder.getLocale()));

                throw new PasswordExpiredException(messageSource.getMessage("message67.txt", null, LocaleContextHolder.getLocale()));
            }

            String access_token =
                    this.jwtUtility.generateAccessToken(userDetails);


            AccessTokens accessTokens = new AccessTokens();
            accessTokens.setToken(access_token);
            accessTokens.setUser(byUsername);
            accessTokens.setExpireDate(jwtUtility.extractExpiration(access_token));
            accessTokensService.saveData(accessTokens);


            String refresh_token =
                    this.jwtUtility.generateRefreshToken(userDetails);

            RefreshTokens refreshTokens = new RefreshTokens();
            refreshTokens.setUser(byUsername);
            refreshTokens.setToken(refresh_token);
            refreshTokens.setExpireDate(jwtUtility.extractExpiration(refresh_token));
            refreshTokenService.saveData(refreshTokens);


            map.put("access token", new CustomJWTResponseDto(access_token));
            map.put("refresh token", new CustomJWTResponseDto(refresh_token));

            return ResponseEntity.ok(map);
        } else {
            throw new InvalidUsernameOrPasswordException(messageSource.getMessage("message57.txt", null, LocaleContextHolder.getLocale()));
        }
    }
}




