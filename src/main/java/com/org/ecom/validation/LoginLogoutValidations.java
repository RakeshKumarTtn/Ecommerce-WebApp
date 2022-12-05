package com.org.ecom.validation;

import com.org.ecom.entity.registration.UserEntity;
import com.org.ecom.exception.PasswordExpiredException;
import com.org.ecom.exception.ResourceNotFoundException;
import com.org.ecom.dto.security.CustomJWTRequestDto;
import com.org.ecom.service.AccessTokensService;
import com.org.ecom.service.RefreshTokenService;
import com.org.ecom.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.rmi.activation.ActivationException;
import java.util.Date;


@Component
public class LoginLogoutValidations {

    @Autowired
    UserService userService;

    @Autowired
    AccessTokensService accessTokensService;

    @Autowired
    RefreshTokenService refreshTokensService;

    @Autowired
    MessageSource messageSource;


    public boolean login_validation(CustomJWTRequestDto jwtRequest) throws ActivationException {

        UserEntity user = userService.findByUsername(jwtRequest.getUsername()).get();

        accessTokensService.expiredToken(new Date());

        refreshTokensService.expiredToken(new Date());

        if (user.getIsExpired().equals(true)) {
            throw new PasswordExpiredException(messageSource.getMessage("message83.txt", null, LocaleContextHolder.getLocale()));
        }

        if (user.getIsLocked().equals(true)) {
            throw new ResourceNotFoundException(messageSource.getMessage("message56.txt", null, LocaleContextHolder.getLocale()));
        }

        if (user.getIsActive().equals(false)) {
            throw new ActivationException(messageSource.getMessage("message81.txt", null, LocaleContextHolder.getLocale()));
        }

        if (!userService.emailAndPasswordExists(user.getEmail(), user.getPassword())) {
            throw new ResourceNotFoundException(messageSource.getMessage("message84.txt", null, LocaleContextHolder.getLocale()));
        }
        return true;
    }
}
