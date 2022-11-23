package com.org.ecom.controller;


import com.org.ecom.dto.SellerDto;
import com.org.ecom.dto.UserDto;
import com.org.ecom.entities.Seller;
import com.org.ecom.entities.UserEntity;
import com.org.ecom.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping(path = "/api/v1/seller")
//@PreAuthorize("hasRole('SELLER','ADMIN')")
public class SellerController {

    @Autowired
    SellerService sellerService;


    @GetMapping("/home")
    ResponseEntity sellerHome() {
        String msg = "Seller Home";
        return new ResponseEntity(msg, HttpStatus.OK);
    }

    @PostMapping("/register")
    public UserDto addSeller(@RequestBody SellerDto sellerDto) {
        return sellerService.addSeller(sellerDto);
    }

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

    /*
        @PatchMapping(path = "/update")
        @ResponseBody
        public ResponseEntity updateUserPatch(@RequestBody UserDto userDto) {
            return userService.updateUserPatch(userDto);
    }*/

}
