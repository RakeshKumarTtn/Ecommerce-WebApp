package com.org.ecom.controller;

import com.org.ecom.dto.SellerDto;
import com.org.ecom.dto.UserDto;
import com.org.ecom.entities.Seller;
import com.org.ecom.entities.User;
import com.org.ecom.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping(path = "/api/v1/ecom/seller")
public class SellerController {

    @Autowired
    SellerService sellerService;

    @PostMapping()
    public UserDto addSeller(@RequestBody SellerDto sellerDto) {
        return sellerService.addSeller(sellerDto);
    }

    @GetMapping("/active")
    public Set<Seller> getAllActiveSeller() {
        return sellerService.getAllActiveSeller();
    }

    @DeleteMapping("/delete/{email}")
    public User deleteSellerByEmail(@PathVariable String email) {
        return sellerService.deleteSellerByEmail(email);
    }

    @DeleteMapping("/delete/username/{username}")
    public User deleteSellerByUserName(@PathVariable String username) {
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
