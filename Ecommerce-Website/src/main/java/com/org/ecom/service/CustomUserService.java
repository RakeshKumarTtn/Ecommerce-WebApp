package com.org.ecom.service;


import com.org.ecom.constant.APPConstant;
import com.org.ecom.dto.CustomerDto;
import com.org.ecom.dto.SellerDto;
import com.org.ecom.entities.*;
import com.org.ecom.enums.RoleLevel;
import com.org.ecom.repository.CustomUsersRepository;
import com.org.ecom.repository.RoleRepository;
import com.org.ecom.security.*;
import com.org.ecom.utility.CustomJwtUtility;
import com.org.ecom.utility.ObjectConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class CustomUserService implements UserDetailsService {

    @Autowired
    private BlackListedTokensService blackListedTokensService;

    @Autowired
    private CustomJwtUtility jwtUtility;

    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    private CustomUsersRepository customUsersRepository;

    @Autowired
    private CustomizedAuditorAware customizedAuditorAware;

    @Autowired
    private ObjectConverter objectConverter;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    @Lazy
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserEntity byUsername = customUsersRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));


        return new User(byUsername.getUsername(), byUsername.getPassword(),
                mapRolesToAuthorities((Set<Role>) byUsername.getRoles()));
    }

    private Collection<GrantedAuthority> mapRolesToAuthorities(Set<Role> rolesSet) {
        return rolesSet.stream().map(role -> new SimpleGrantedAuthority(role.getAuthority())).collect(Collectors.toSet());
    }

    public void registerSeller(SellerDto sellerDto) {

        customizedAuditorAware.setName(sellerDto.getFirstName());

        Seller seller = objectConverter.dtoToEntity(sellerDto);

        Optional<Role> byAuthority = roleRepository.findByAuthority(String.valueOf(RoleLevel.SELLER));

        byAuthority.ifPresent(seller::addRole);

        customUsersRepository.save(seller);
    }

    public void registerCustomer(CustomerDto customerDto) {
        customizedAuditorAware.setName(customerDto.getFirstName());

        Customer customer = objectConverter.dtoToEntity(customerDto);

        Optional<Role> byAuthority = roleRepository.findByAuthority(String.valueOf(RoleLevel.CUSTOMER));

        byAuthority.ifPresent(customer::addRole);
        customUsersRepository.save(customer);

    }

    public CustomMailRequest loadUserByUsernameAndEmail(String username, String email) {
        UserEntity findByUsernameAndEmail = customUsersRepository.findByUsernameAndEmail(username, email)
                .orElseThrow(() -> new UsernameNotFoundException("Username or Email not found"));

        return new CustomMailRequest(findByUsernameAndEmail.getUsername(), findByUsernameAndEmail.getEmail());
    }

    public ResponseEntity<String> resetPassword(CustomResetPassword customResetPassword) {
        Date diff = new Date(jwtUtility.extractExpiration(customResetPassword.getJwtToken()).getTime()
                - jwtUtility.extractIssuedAt(customResetPassword.getJwtToken()).getTime());

        Date date = new Date(1000 * 60 * 5);

        if (diff.getTime() == date.getTime()) {
            UserEntity user = customUsersRepository.findByUsername(customResetPassword.getUsername()).get();
            user.setPassword(bCryptPasswordEncoder.encode(customResetPassword.getPassword()));
            customUsersRepository.save(user);

            emailSenderService.sendEmail(user.getEmail(),
                    "[Ecommerce] Your password was reset",
                    String.valueOf(APPConstant.AFTER_RESET_EMAIL_MESSAGE.insert(8, user.getUsername())));

            return ResponseEntity.ok("Password is successfully updated");
        }
        return ResponseEntity.of(Optional.of("Reset token has expired"));
    }


    public ResponseEntity<String> forgotPassword(CustomMailRequest customMailRequest) {

        CustomMailRequest customMailRequest1 = loadUserByUsernameAndEmail(customMailRequest.getUsername(), customMailRequest.getEmail());

        String resetToken =
                this.jwtUtility.generateResetToken(customMailRequest1);

        emailSenderService.sendEmail(customMailRequest.getEmail(),
                "[Ecommerce] Please reset your password",
                String.valueOf(APPConstant.BEFORE_RESET_EMAIL_MESSAGE.insert(174, "\n\n" + new CustomJWTResponse(resetToken).getJwtToken())));

        return ResponseEntity.ok("Reset Token sent on email.");
    }

    public String logout(String token) {
        Date diff = new Date(jwtUtility.extractExpiration(token).getTime() - jwtUtility.extractIssuedAt(token).getTime());

        Date date = new Date(1000 * 60 * 10);

        if (diff.getTime() == date.getTime()) {
            BlackListedTokens blackListedTokens = new BlackListedTokens();
            blackListedTokens.setJwtToken(token);
            blackListedTokensService.saveToken(blackListedTokens);
            return "Logout Successful";
        }
        throw new RuntimeException("Provide valid Access Token");
    }

    public ResponseEntity<CustomJWTResponse> generate_access_token(CustomJWTResponse customJWTResponse) {
        String jwtToken = customJWTResponse.getJwtToken();

        Date diff = new Date(jwtUtility.extractExpiration(jwtToken).getTime() - jwtUtility.extractIssuedAt(jwtToken).getTime());

        Date date = new Date(1000 * 60 * 60 * 24);

        if (diff.getTime() == date.getTime()) {
            if (jwtUtility.extractExpiration(jwtToken).after(new Date(System.currentTimeMillis()))) {
                String username = jwtUtility.extractUsername(jwtToken);

                UserDetails userDetails = loadUserByUsername(username);

                String access_token =
                        this.jwtUtility.generateAccessToken(userDetails);

                return ResponseEntity.ok(new CustomJWTResponse(access_token));
            } else {
                throw new RuntimeException("Refresh Token expired. Generate new Refresh Token and try again");
            }
        } else {
            throw new RuntimeException("Provide a valid Refresh Token");
        }

    }

    public ResponseEntity<Map<String, CustomJWTResponse>> login(CustomJWTRequest jwtRequest) {
        UserDetails userDetails = loadUserByUsername(jwtRequest.getUsername());

        String access_token =
                this.jwtUtility.generateAccessToken(userDetails);


        String refresh_token =
                this.jwtUtility.generateRefreshToken(userDetails);


        Map<String, CustomJWTResponse> map = new Hashtable();

        map.put("access token", new CustomJWTResponse(access_token));
        map.put("refresh token", new CustomJWTResponse(refresh_token));

        return ResponseEntity.ok(map);
    }
}
