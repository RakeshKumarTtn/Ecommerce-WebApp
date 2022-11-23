package com.org.ecom.controller;

import com.org.ecom.dto.CustomerDto;
import com.org.ecom.dto.SellerDto;
import com.org.ecom.service.CustomUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/register")

public class RegisterController {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private CustomUserService customUserService;

    @PostMapping("/seller")
    public String registerSeller(@RequestBody SellerDto sellerDto) {
        sellerDto.setPassword(passwordEncoder.encode(sellerDto.getPassword()));
        customUserService.registerSeller(sellerDto);
        return "Registration successful";
    }

    @PostMapping("/customer")
    public String registerCustomer(@RequestBody CustomerDto customerDto) {
        customerDto.setPassword(passwordEncoder.encode(customerDto.getPassword()));
        customUserService.registerCustomer(customerDto);
        return "Registration successful";
    }
}
