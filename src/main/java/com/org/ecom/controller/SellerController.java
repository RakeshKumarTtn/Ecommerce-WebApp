package com.org.ecom.controller;


import com.org.ecom.entity.registration.Address;
import com.org.ecom.entity.registration.Seller;
import com.org.ecom.dto.security.CustomJWTResponseDto;
import com.org.ecom.service.SellerService;
import com.org.ecom.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/api/v1/seller")
public class SellerController {

    @Autowired
    SellerService sellerService;

    @Autowired
    UserService userService;

    //Seller Home API
    @GetMapping("/home")
    public ResponseEntity adminHome() {
        String msg = "Seller Home";
        return new ResponseEntity(msg, HttpStatus.OK);
    }

    //My_Profile API for getting all the Mandatory Information from the Seller
    @GetMapping("/my_profile")
    public MappingJacksonValue viewMyProfile(@RequestBody CustomJWTResponseDto customJWTResponse) {
        return sellerService.sellerData(customJWTResponse.getJwtToken());
    }

    //Update_Seller_Profile API for Updating the Seller Information
    @PatchMapping("/update-seller-profile")
    public ResponseEntity<String> updateSellerProfile(@RequestBody Seller seller) {
        Seller loggedInSeller = userService.getLoggedInSeller();
        Long id = loggedInSeller.getUserId();
        return sellerService.updateSellerProfile(id, seller);
    }

    //Update_Seller_Password API for Updating the Seller Password
    @PatchMapping("/update-seller-password")
    public ResponseEntity<String> updatePassword(@Valid @RequestParam String password, @Valid @RequestParam String confirmPassword) {
        Seller loggedInSeller = userService.getLoggedInSeller();
        Long id = loggedInSeller.getUserId();
        return sellerService.updatePassword(id, password, confirmPassword);
    }

    //Update_Seller_Address API for Updating the Seller Address Information
    @PatchMapping("/update-seller-address/{address_id}")
    public ResponseEntity<String> updateAddress(@PathVariable("address_id") Integer address_id, @RequestBody Address address) {
        Seller loggedInSeller = userService.getLoggedInSeller();
        Long id = loggedInSeller.getUserId();
        return sellerService.updateAddress(id, address_id, address);
    }
}
