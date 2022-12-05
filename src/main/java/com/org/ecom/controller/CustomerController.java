package com.org.ecom.controller;


import com.org.ecom.entity.registration.Address;
import com.org.ecom.entity.registration.Customer;
import com.org.ecom.repository.registration.CustomerRepository;
import com.org.ecom.dto.security.CustomJWTResponseDto;
import com.org.ecom.service.CustomerService;
import com.org.ecom.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/customer")
public class CustomerController {

    @Autowired
    CustomerService customerService;

    @Autowired
    UserService userService;

    @Autowired
    CustomerRepository customerRepository;

    //API for Customer Home
    @GetMapping("/home")
    public ResponseEntity<String> customerHome() {
        String customerHome = "Inside Customer Home";
        return new ResponseEntity(customerHome, HttpStatus.OK);
    }

    //API for getting the Customer Profile displaying the Mandatory Information
    @GetMapping("/my_profile")
    public MappingJacksonValue viewMyProfile(@RequestBody CustomJWTResponseDto customJWTResponse) {
        return customerService.customerData(customJWTResponse.getJwtToken());
    }

    //API for getting the Customer Addresses
    @GetMapping("/view-customer-addresses")
    public MappingJacksonValue viewCustomerAddresses(@RequestBody CustomJWTResponseDto customJWTResponse) {
        return customerService.viewCustomerAddresses(customJWTResponse.getJwtToken());
    }

    //API for updating the Customer Information
    @PostMapping("/update-customer-profile")
    public ResponseEntity<String> updateCustomerProfile(@RequestBody Customer customer) {
        Customer loggedInCustomer = userService.getLoggedInCustomer();
        return customerService.updateCustomerProfile(loggedInCustomer, customer);
    }

    //API for updating the Logged In Customer Password
    @PatchMapping("/update-customer-password")
    public ResponseEntity<String> updatePassword(@Valid @RequestParam String password, @Valid @RequestParam String confirmPassword) {
        Customer loggedInCustomer = userService.getLoggedInCustomer();
        Long id = loggedInCustomer.getUserId();
        return customerService.updatePassword(id, password, confirmPassword);
    }

    //API for updating the Customer Address
    @PatchMapping("/update-customer-address/{address_id}")
    public ResponseEntity<String> updateAddress(@PathVariable Integer address_id, @RequestBody Address address) {
        Customer loggedInCustomer = userService.getLoggedInCustomer();
        Long id = loggedInCustomer.getUserId();
        return customerService.updateAddress(id, address_id, address);
    }

    //API for Adding the Customer Address
    @PostMapping("/add-customer-address")
    public ResponseEntity<String> addAddressForUser(@RequestBody Address address) {
        Customer loggedInCustomer = userService.getLoggedInCustomer();
        Long id = loggedInCustomer.getUserId();
        return customerService.addAddressForUser(id, address);
    }

    //API for Deleting the Customer Address
    @DeleteMapping("/delete-customer-address/{address_id}")
    public ResponseEntity<String> deleteAddress(@PathVariable Integer address_id) {
        Customer loggedInCustomer = userService.getLoggedInCustomer();
        Long id = loggedInCustomer.getUserId();
        return customerService.deleteAddressForUser(id, address_id);
    }
}
