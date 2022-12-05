package com.org.ecom.validation;

import com.org.ecom.entity.registration.UserEntity;
import com.org.ecom.exception.*;
import com.org.ecom.dto.security.CustomMailRequestDto;
import com.org.ecom.dto.security.CustomResetPasswordDto;
import com.org.ecom.service.ResetTokenService;
import com.org.ecom.service.UserService;
import com.org.ecom.utility.CustomJwtUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.rmi.activation.ActivationException;
import java.util.Date;

@Component
public class ForgotPasswordValidations {

    @Autowired
    private UserService userService;

    @Autowired
    private CustomJwtUtility jwtUtility;

    @Autowired
    private ResetTokenService resetTokenService;

    @Autowired
    MessageSource messageSource;

    public CustomResetPasswordDto resetValidations(CustomResetPasswordDto customResetPassword) {
        String jwtToken = customResetPassword.getJwtToken();
        String username = jwtUtility.extractUsername(jwtToken);
        UserEntity userEntity = userService.findByUsername(username).get();

        if (!resetTokenService.findByToken(jwtToken).isPresent()) {
            throw new InvalidTokenException(messageSource.getMessage("message79.txt", null, LocaleContextHolder.getLocale()));
        }

        if (jwtUtility.extractExpiration(jwtToken).before(new Date())) {
            resetTokenService.deleteToken(userEntity.getUserId(), jwtToken);
            throw new TokenExpiredException(messageSource.getMessage("message80.txt", null, LocaleContextHolder.getLocale()));
        }

        if (!customResetPassword.getPassword().equals(customResetPassword.getConfirmPassword())) {
            throw new PasswordMatchException(messageSource.getMessage("message45.txt", null, LocaleContextHolder.getLocale()));
        }

        return customResetPassword;
    }

    public boolean forget_password_validation(CustomMailRequestDto customMailRequest) throws ActivationException {

        UserEntity user = userService.findByUsername(customMailRequest.getUsername()).get();

        if (user.getIsLocked().equals(true)) {
            throw new ResourceNotFoundException(messageSource.getMessage("message5.txt", null, LocaleContextHolder.getLocale()));
        }

        if (user.getIsActive().equals(false)) {
            throw new ActivationException(messageSource.getMessage("message81.txt", null, LocaleContextHolder.getLocale()));
        }

        if (!userService.emailExists(user.getEmail())) {
            throw new ResourceNotFoundException(messageSource.getMessage("message82.txt", null, LocaleContextHolder.getLocale()));
        }
        return true;
    }
}
