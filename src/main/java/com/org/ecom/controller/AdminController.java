package com.org.ecom.controller;

import com.org.ecom.repository.registration.CustomerRepository;
import com.org.ecom.repository.registration.SellerRepository;
import com.org.ecom.service.AdminService;
import com.org.ecom.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private UserService userService;

    @Autowired
    AdminService adminService;

    //API for Admin Home
    @GetMapping("/home")
    public ResponseEntity adminHome() {
        String msg = "Admin Home";
        return new ResponseEntity(msg, HttpStatus.OK);
    }

    //API For getting the Registered Customer
    @GetMapping("/registered/customers/{email}")
    public MappingJacksonValue registeredCustomers(@PathVariable("email") String email) {
        return adminService.listAllCustomers(email);
    }

    //API For getting the Registered Seller
    @GetMapping("/registered/sellers/{email}")
    public MappingJacksonValue registeredSellers(@PathVariable("email") String email) {
        return adminService.listAllSellers(email);
    }

    //API For getting all Registered Active Customers list
    @GetMapping("/admin/listAllCustomers")
    public MappingJacksonValue getAllCustomers(@RequestParam(name = "pageNo", required = true, defaultValue = "0") Integer pageNo,
                                               @RequestParam(name = "pageSize", required = true, defaultValue = "10") Integer pageSize,
                                               @RequestParam(name = "sortBy", defaultValue = "userId") String sortBy) {
        return adminService.getAllRegisteredCustomers(pageNo, pageSize, sortBy);
    }

    //API For getting all Registered Active Sellers list
    @GetMapping("/admin/listAllSellers")
    public MappingJacksonValue getAllSellers(@RequestParam(name = "pageNo", required = true, defaultValue = "0") Integer pageNo,
                                             @RequestParam(name = "pageSize", required = true, defaultValue = "10") Integer pageSize,
                                             @RequestParam(name = "sortBy", defaultValue = "userId") String sortBy) {
        return adminService.getAllRegisteredSellers(pageNo, pageSize, sortBy);
    }

    //API For Activating the Customer Account
    @PutMapping("/admin/activate_customer")
    public ResponseEntity<String> activateCustomer(@RequestParam("id") Long id) {
        return adminService.activateCustomer(id);
    }

    //API for De_Activating the Customer Account
    @PutMapping("/admin/deactivate_customer")
    public ResponseEntity<String> deactivateCustomer(@RequestParam("id") Long id, HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        String username = principal.getName();
        return adminService.deactivateCustomer(username, id);
    }

    //API For Activating the Seller Account
    @PutMapping("/admin/activate_seller")
    public ResponseEntity<String> activateSeller(@RequestParam("id") Long id) {
        return adminService.activateSeller(id);
    }

    //API For De_Activating the Seller Account
    @PutMapping("/admin/deactivate_seller")
    public ResponseEntity<String> deactivateSeller(@RequestParam("id") Long id) {
        return adminService.deactivateSeller(id);
    }

    //API for Locking the User Account
    @PutMapping("/admin/lock_user")
    public ResponseEntity<String> lockUser(@RequestParam("id") Long id) {
        return adminService.lockUser(id);
    }

    //API for Un locking the User Account
    @PutMapping("/admin/unlock_user")
    public ResponseEntity<String> unlockUser(@RequestParam("id") Long id) {
        return adminService.unlockUser(id);
    }
}
