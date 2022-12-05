package com.org.ecom.controller;

import antlr.debug.MessageEvent;
import com.org.ecom.dto.login.CustomerDto;
import com.org.ecom.dto.login.EmailDto;
import com.org.ecom.dto.login.SellerDto;
import com.org.ecom.dto.security.CustomJWTResponseDto;
import com.org.ecom.service.CustomerService;
import com.org.ecom.service.EmailSenderService;
import com.org.ecom.service.SellerService;
import com.org.ecom.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/register")

public class RegisterController {

    @Autowired
    EmailSenderService emailSenderService;

    @Autowired
    private UserService userService;

    @Autowired
    private SellerService sellerService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    MessageSource messageSource;

    //Register Seller API for Registration of Seller
    @PostMapping("/seller")
    public String registerSeller(@Valid @RequestBody SellerDto sellerDto) {
        System.out.println(sellerDto);
        sellerService.registerSeller(sellerDto);
        return messageSource.getMessage("message51.txt", null, LocaleContextHolder.getLocale());
    }

    //Register Customer API for Registration of Customer
    @PostMapping("/customer")
    public String registerCustomer(@Valid @RequestBody CustomerDto customerDto) {
        customerService.registerCustomer(customerDto);
        return messageSource.getMessage("message52.txt", null, LocaleContextHolder.getLocale());
    }

    //Activate Customer/Seller account API for activating the Accounts
    @PutMapping("/activate_account")
    public ResponseEntity<String> activate_account(@RequestBody CustomJWTResponseDto customJWTResponse) {
        return userService.activate_account(customJWTResponse);
    }

    //Resent_Activation_Link API if the activation token expired then send another token in the mail
    @PostMapping("/resend_activation_link")
    public ResponseEntity<String> resend_activation_link(@Valid @RequestBody EmailDto emailDto) {
        return userService.resendActivationLink(emailDto.getEmail());
    }
}
