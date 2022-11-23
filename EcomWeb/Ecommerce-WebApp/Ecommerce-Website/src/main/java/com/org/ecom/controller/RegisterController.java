package com.org.ecom.controller;

import com.org.ecom.dto.CustomerDto;
import com.org.ecom.dto.SellerDto;
import com.org.ecom.security.CustomizedAuditorAware;
import com.org.ecom.service.CustomUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;

@RestController
@RequestMapping("/api/v1/register")

public class RegisterController {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private CustomUserService customUserService;

    @Autowired
    CustomizedAuditorAware customizedAuditorAware;

    @PostMapping("/seller")
    public String registerSeller(@RequestBody SellerDto sellerDto) {
        sellerDto.setPassword(passwordEncoder.encode(sellerDto.getPassword()));
        customizedAuditorAware.setName(sellerDto.getFirstName());
        customUserService.registerSeller(sellerDto);
        return "You are successfully registered as a seller. You can activate your account within next 24 hours.";
    }

    @PostMapping("/customer")
    public String registerCustomer(@RequestBody CustomerDto customerDto) {
        customerDto.setPassword(passwordEncoder.encode(customerDto.getPassword()));
        customizedAuditorAware.setName(customerDto.getFirstName());
        customUserService.registerCustomer(customerDto);
        return "You are successfully registered as a customer. You can activate your account within next 24 hours.";
    }
}
