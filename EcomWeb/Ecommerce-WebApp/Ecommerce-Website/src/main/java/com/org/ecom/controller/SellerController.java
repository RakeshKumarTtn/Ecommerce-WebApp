package com.org.ecom.controller;


import com.org.ecom.dto.*;
import com.org.ecom.entities.Seller;
import com.org.ecom.entities.UserEntity;
import com.org.ecom.repository.CustomerRepository;
import com.org.ecom.repository.SellerRepository;
import com.org.ecom.service.SellerService;
import com.org.ecom.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping(path = "/api/v1/seller")
public class SellerController {

    @Autowired
    SellerRepository sellerRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    UserService userService;

    @Autowired
    SellerService sellerService;

    CustomerDto customerDto;

    @GetMapping("/home")
    public ResponseEntity sellerHome() {
        String msg = "Seller Home";
        return new ResponseEntity(msg, HttpStatus.OK);
    }

    @GetMapping("/seller/profile")
    public SellerProfileDto viewProfile() {
        return sellerService.viewProfile();
    }

    @PatchMapping("/seller/profile/update")
    ResponseEntity updateProfile(@RequestBody SellerDto sellerDto) {
        return sellerService.updateProfile(sellerDto);
    }

   /* @PatchMapping("/seller/password/update")
    String updatePassword(@RequestParam("password") String newPassword) {
        return userService.updatePassword(newPassword);
    }

    @PutMapping("/seller/address/update/{id}")
    String updateAddress(@Valid @RequestBody AddressDto addressDto, @PathVariable Long id) {
        userService.updateAddress(id, addressDto);
        return "Address with id " + id + " updated successfully";
    }*/

    @GetMapping("/active")
    public Set<Seller> getAllActiveSeller() {
        return sellerService.getAllActiveSeller();
    }

    @DeleteMapping("/delete/{email}")
    public UserEntity deleteSellerByEmail(@PathVariable String email) {
        return sellerService.deleteSellerByEmail(email);
    }

    @DeleteMapping("/delete/username/{username}")
    public UserEntity deleteSellerByUserName(@PathVariable String username) {
        return sellerService.deleteSellerByUserName(username);
    }

    @PutMapping("/update/{email}")
    @Modifying
    public UserDto updateSeller(@RequestBody SellerDto sellerDto, @PathVariable String email) {
        return sellerService.updateSeller(sellerDto, email);
    }
}