package com.org.ecom.service;

import com.org.ecom.entity.security.ActivationToken;
import com.org.ecom.entity.registration.Customer;
import com.org.ecom.entity.registration.Seller;
import com.org.ecom.entity.registration.UserEntity;
import com.org.ecom.exception.InvalidTokenException;
import com.org.ecom.exception.ResourceNotFoundException;
import com.org.ecom.exception.TokenExpiredException;
import com.org.ecom.repository.registration.UserRepository;
import com.org.ecom.dto.security.CustomJWTResponseDto;
import com.org.ecom.utility.CustomJwtUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    private CustomJwtUtility jwtUtility;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    ActivationTokenService activationTokenService;

    @Autowired
    MessageSource messageSource;

    private Long[] l = {};

    public Optional<UserEntity> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<UserEntity> findByUserId(Long id) {
        return userRepository.findById(id);
    }

    @Transactional
    public ResponseEntity<String> activate_account(CustomJWTResponseDto customJWTResponse) {

        String jwtToken = customJWTResponse.getJwtToken();

        Date date_expired = jwtUtility.extractExpiration(jwtToken);

        Date current_date = new Date();


        String username = jwtUtility.extractUsername(jwtToken);
        UserEntity user = findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username not found"));

        if (activationTokenService.findByToken(jwtToken).isPresent()) {

            if (date_expired.after(current_date)) {

                userRepository.updateByIsActive(user.getEmail(), true);

                activationTokenService.deleteToken(user.getUserId(), jwtToken);

                emailSenderService.sendEmail(user.getEmail(),
                        messageSource.getMessage("message53.txt", null, LocaleContextHolder.getLocale()),
                        messageSource.getMessage("message71.txt", null, LocaleContextHolder.getLocale()));


                return ResponseEntity.ok(messageSource.getMessage("message71.txt", null, LocaleContextHolder.getLocale()));
            } else {
                String resetToken = jwtUtility.generateResetToken(user);

                activationTokenService.deleteToken(user.getUserId(), jwtToken);

                ActivationToken activationToken = new ActivationToken();
                activationToken.setToken(resetToken);
                activationToken.setUser(user);

                activationTokenService.saveData(activationToken);

                emailSenderService.sendEmail(user.getEmail(),
                        messageSource.getMessage("message53.txt", null, LocaleContextHolder.getLocale()),
                        messageSource.getMessage("message41.txt", null, LocaleContextHolder.getLocale()) + "\n" + resetToken);

                throw new TokenExpiredException(messageSource.getMessage("message72.txt", null, LocaleContextHolder.getLocale()));
            }
        } else {
            throw new InvalidTokenException(messageSource.getMessage("message13.txt", null, LocaleContextHolder.getLocale()));
        }
    }


    @Transactional
    public Boolean locked(String username) {
        Optional<UserEntity> userEntity = userRepository.findByUsername(username);
        int invalidAttemptCount = userEntity.get().getInvalidAttemptCount();
        if (invalidAttemptCount > 1) {
            userRepository.updateInvalidAttemptCount(username, invalidAttemptCount - 1);
            return true;
        } else {
            userEntity.get().setInvalidAttemptCount(0);
            userEntity.get().setIsLocked(true);
            userRepository.save(userEntity.get());

            emailSenderService.sendEmail(userEntity.get().getEmail(),
                    messageSource.getMessage("message73.txt", null, LocaleContextHolder.getLocale()),
                    messageSource.getMessage("message74.txt", null, LocaleContextHolder.getLocale()));

            return false;
        }
    }

    public ResponseEntity<String> resendActivationLink(String email) {

        UserEntity userEntity = userRepository.findByEmail(email).get();

        if (userRepository.existsByEmail(email)) {

            if (userEntity.getIsActive().equals(true)) {
                return ResponseEntity.ok(messageSource.getMessage("message75.txt", null, LocaleContextHolder.getLocale()));
            }

            String resetToken = jwtUtility.generateResetToken(userEntity);

            ActivationToken activationToken = new ActivationToken();
            activationToken.setToken(resetToken);
            activationToken.setUser(userEntity);

            activationTokenService.saveData(activationToken);

            emailSenderService.sendEmail(email,
                    messageSource.getMessage("message53.txt", null, LocaleContextHolder.getLocale()),
                    messageSource.getMessage("message41.txt", null, LocaleContextHolder.getLocale()) + "\n" + resetToken);

            return ResponseEntity.ok(messageSource.getMessage("message76.txt", null, LocaleContextHolder.getLocale()));
        } else {
            throw new ResourceNotFoundException(messageSource.getMessage("message77.txt", null, LocaleContextHolder.getLocale()));
        }
    }

    public Boolean emailAndPasswordExists(String email, String password) {
        return userRepository.existsByEmailAndPassword(email, password);
    }

    public Boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    public Customer getLoggedInCustomer() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = null;
        if (authentication.getPrincipal() instanceof UserDetails) {
            username = ((UserDetails) authentication.getPrincipal()).getUsername();
        } else {
            username = authentication.getPrincipal().toString();
        }
        UserEntity byEmailUser = userRepository.findByUsername(username).get();
        Customer customer = (Customer) byEmailUser;
        return customer;
    }

    public Seller getLoggedInSeller() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = null;
        if (authentication.getPrincipal() instanceof UserDetails) {
            username = ((UserDetails) authentication.getPrincipal()).getUsername();
        } else {
            username = authentication.getPrincipal().toString();
        }

        UserEntity byEmailUser = userRepository.findByUsername(username).get();
        Seller seller = (Seller) byEmailUser;
        return seller;
    }
}
