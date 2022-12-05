package com.org.ecom.validation;

import com.org.ecom.dto.login.CustomerDto;
import com.org.ecom.dto.login.SellerDto;
import com.org.ecom.exception.EmailOrUsernameAlreadyExists;
import com.org.ecom.exception.PasswordMatchException;
import com.org.ecom.repository.registration.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
public class RegisterValidations {

    @Autowired
    MessageSource messageSource;

    @Autowired
    UserRepository userRepository;

    public Boolean passwordAndConfirmPasswordMatchSeller(SellerDto sellerDto) {
        if (!sellerDto.getPassword().equals(sellerDto.getConfirmPassword())) {

            throw new PasswordMatchException(messageSource.getMessage("message85.txt", null, LocaleContextHolder.getLocale()));
        }

        if(userRepository.existsByEmail(sellerDto.getEmail())){
            throw new EmailOrUsernameAlreadyExists("User is already registered with this email");
        }

        if(userRepository.findByUsername(sellerDto.getUsername()).isPresent()){
            throw new EmailOrUsernameAlreadyExists("User is already registered with this username");
        }


        return true;
    }

    public Boolean passwordAndConfirmPasswordMatchCustomer(CustomerDto customerDto) {
        if (!customerDto.getPassword().equals(customerDto.getConfirmPassword())) {

            throw new PasswordMatchException(messageSource.getMessage("message85.txt", null, LocaleContextHolder.getLocale()));
        }

        if(userRepository.existsByEmail(customerDto.getEmail())){
            throw new EmailOrUsernameAlreadyExists("User is already registered with this email");
        }

        if(userRepository.findByUsername(customerDto.getUsername()).isPresent()){
            throw new EmailOrUsernameAlreadyExists("User is already registered with this username");
        }

        return true;
    }
}