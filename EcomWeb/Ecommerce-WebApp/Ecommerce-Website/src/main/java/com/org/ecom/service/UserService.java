package com.org.ecom.service;

import com.org.ecom.entities.UserEntity;
import com.org.ecom.exception.UserNotFoundException;
import com.org.ecom.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    EmailSenderService emailSenderService;

    @Autowired
    MessageSource messageSource;

    private Long[] l = {};

    public UserEntity getProfile(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    public Boolean findByUsername(String username) {
        Optional<UserEntity> userEntity = userRepository.findByUsername(username);
        if (userEntity.get().getInvalidAttemptCount() > 1) {
            userRepository.updateInvalidAttemptCount(username);
        } else {
            userEntity.get().setInvalidAttemptCount(0);
            userEntity.get().setIsLocked(true);
            userRepository.save(userEntity.get());
            return false;
        }
        return true;
    }

    public ResponseEntity activateCustomer(@RequestParam long customerId) {
        Optional<UserEntity> getuser = userRepository.findById(customerId);

        if (!getuser.isPresent()) {
            throw new UserNotFoundException(messageSource.getMessage("message30.txt", l, LocaleContextHolder.getLocale()) + customerId);
        } else {
            UserEntity user = getuser.get();
            if (user.getIsActive() == false) {
                user.setIsActive(true);
                userRepository.save(user);
                emailSenderService.sendEmail(user.getEmail(), "Your Ecommerce Account has been activated", user.getFirstName() + user.getLastName() + " Welcome to Ecommerce-App \nAccount Activated Successfully");
                return ResponseEntity.ok(messageSource.getMessage("message32.txt", l, LocaleContextHolder.getLocale()));
            } else
                return ResponseEntity.ok(messageSource.getMessage("message31.txt", l, LocaleContextHolder.getLocale()));
        }
    }

    public ResponseEntity deactivateCustomer(@RequestParam long customerId) {
        Optional<UserEntity> getuser = userRepository.findById(customerId);
        if (!getuser.isPresent()) {
            throw new UserNotFoundException(messageSource.getMessage("message30.txt", l, LocaleContextHolder.getLocale()) + customerId);
        } else {
            UserEntity user = getuser.get();
            if (user.getIsActive() == true) {
                user.setIsActive(false);
                userRepository.save(user);

                emailSenderService.sendEmail(user.getEmail(), "Your Ecommerce Account has been deactivated", "Dear " + user.getFirstName() + " " + user.getLastName() + " Your Ecommerce-App Account de-Activated Successfully");
                return ResponseEntity.ok(messageSource.getMessage("message33.txt", l, LocaleContextHolder.getLocale()));
            } else
                return ResponseEntity.ok(messageSource.getMessage("message34.txt", l, LocaleContextHolder.getLocale()));
        }
    }

    public ResponseEntity activateSeller(@RequestParam long sellerId) {
        Optional<UserEntity> getuser = userRepository.findById(sellerId);

        if (!getuser.isPresent()) {
            throw new UserNotFoundException(messageSource.getMessage("message30.txt", l, LocaleContextHolder.getLocale()) + sellerId);
        } else {
            UserEntity user = getuser.get();
            if (user.getIsActive() == false) {
                user.setIsActive(true);
                userRepository.save(user);
                emailSenderService.sendEmail(user.getEmail(), "Your Ecommerce Account has been activated", user.getFirstName() + user.getLastName() + " Welcome to Ecommerce-App \nAccount Activated Successfully");
                return ResponseEntity.ok(messageSource.getMessage("message32.txt", l, LocaleContextHolder.getLocale()));
            } else
                return ResponseEntity.ok(messageSource.getMessage("message31.txt", l, LocaleContextHolder.getLocale()));
        }
    }

    public ResponseEntity deactivateSeller(@RequestParam long sellerId) {
        Optional<UserEntity> getuser = userRepository.findById(sellerId);
        if (!getuser.isPresent()) {
            throw new UserNotFoundException(messageSource.getMessage("message30.txt", l, LocaleContextHolder.getLocale()) + sellerId);
        } else {
            UserEntity user = getuser.get();
            if (user.getIsActive() == true) {
                user.setIsActive(false);
                userRepository.save(user);

                emailSenderService.sendEmail(user.getEmail(), "Your Ecommerce Account has been deactivated", "Dear " + user.getFirstName() + " " + user.getLastName() + " Your Ecommerce-App Account de-Activated Successfully");
                return ResponseEntity.ok(messageSource.getMessage("message33.txt", l, LocaleContextHolder.getLocale()));
            } else
                return ResponseEntity.ok(messageSource.getMessage("message34.txt", l, LocaleContextHolder.getLocale()));
        }
    }
}
