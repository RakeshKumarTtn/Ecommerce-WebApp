package com.org.ecom.controller;

import com.org.ecom.dto.CustomerDto;
import com.org.ecom.dto.UserDto;
import com.org.ecom.entities.Customer;
import com.org.ecom.entities.UserEntity;
import com.org.ecom.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping(path = "/api/v1/ecom/customer")
public class CustomerController {

    @Autowired
    CustomerService customerService;

    @GetMapping("/home")
    ResponseEntity customerHome() {
        String msg = "Customer Home";
        return new ResponseEntity(msg, HttpStatus.OK);
    }

    @PostMapping("/add")
    public UserDto addCustomer(@RequestBody CustomerDto customerDto) {
        return customerService.addCustomer(customerDto);
    }

    @GetMapping("/active")
    public Set<Customer> getAllActiveCustomer() {
        return customerService.getAllActiveCustomer();
    }

    @DeleteMapping("/delete/{email}")
    public UserEntity deleteCustomerByEmail(@PathVariable String email) {
        return customerService.deleteCustomerByEmail(email);
    }

    @DeleteMapping("/delete/username/{username}")
    public UserEntity deleteCustomerByUserName(@PathVariable String username) {
        return customerService.deleteCustomerByUserName(username);
    }

    @PutMapping("/update/{email}")
    @Modifying
    public UserDto updateCustomer(@RequestBody CustomerDto customerDto, @PathVariable String email) {
        return customerService.updateCustomer(customerDto, email);
    }

    /*
        @PatchMapping(path = "/update")
        @ResponseBody
        public ResponseEntity updateUserPatch(@RequestBody UserDto userDto) {
            return userService.updateUserPatch(userDto);
    }*/
}
