package com.org.ecom.validation;

import com.org.ecom.entity.registration.Customer;
import com.org.ecom.entity.registration.Seller;
import com.org.ecom.exception.InvalidUserIdException;
import com.org.ecom.service.CustomerService;
import com.org.ecom.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AdminActivationValidations {

    @Autowired
    CustomerService customerService;

    @Autowired
    SellerService sellerService;

    @Autowired
    MessageSource messageSource;

    public Boolean ActivationValidationsCustomer(Long id) {

        Optional<Customer> byUserId = customerService.findByUserId(id);
        if (!byUserId.isPresent()) {
            throw new InvalidUserIdException(messageSource.getMessage("message78.txt", null, LocaleContextHolder.getLocale()));
        } else if (byUserId.get().getIsActive()) {
            return false;
        } else {
            return true;
        }
    }

    public Boolean DeactivationValidationsCustomer(Long id) {

        Optional<Customer> byUserId = customerService.findByUserId(id);
        if (!byUserId.isPresent()) {
            throw new InvalidUserIdException(messageSource.getMessage("message78.txt", null, LocaleContextHolder.getLocale()));
        } else if (!byUserId.get().getIsActive()) {
            return false;
        } else {
            return true;
        }
    }

    public Boolean ActivationValidationsSeller(Long id) {

        Optional<Seller> byUserId = sellerService.findByUserId(id);
        if (!byUserId.isPresent()) {
            throw new InvalidUserIdException(messageSource.getMessage("message78.txt", null, LocaleContextHolder.getLocale()));
        } else if (byUserId.get().getIsActive()) {
            return false;
        } else {
            return true;
        }
    }

    public Boolean DeactivationValidationsSeller(Long id) {

        Optional<Seller> byUserId = sellerService.findByUserId(id);
        if (!byUserId.isPresent()) {
            throw new InvalidUserIdException(messageSource.getMessage("message78.txt", null, LocaleContextHolder.getLocale()));
        } else if (!byUserId.get().getIsActive()) {
            return false;
        } else {
            return true;
        }
    }
}
