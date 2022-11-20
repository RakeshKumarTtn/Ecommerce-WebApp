/*
package com.org.ecom.controller;

import com.org.ecom.entities.UserEntity;
import com.org.ecom.repository.CustomerRepository;
import com.org.ecom.repository.SellerRepository;
import com.org.ecom.service.UserService;
import com.org.ecom.utility.Criteria;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class AdminController {
    //All working

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    Criteria criteria;

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private AdminService adminService;

    @ApiOperation(value = "home page for admin")
    @GetMapping("/admin/home")
    public ResponseEntity adminHome() {
        String msg = "Admin home";
        return new ResponseEntity(msg, HttpStatus.OK);
    }

    @GetMapping("/admin/listAllCustomers")
    public List<RegisteredCustomerDto> getAllCustomers(@RequestParam(name = "pageNo", required = true, defaultValue = "0") Integer pageNo,
                                                       @RequestParam(name = "pageSize", required = true, defaultValue = "10") Integer pageSize,
                                                       @RequestParam(name = "sortBy", defaultValue = "id") String sortBy) {
        return adminService.getAllRegisteredCustomers(pageNo, pageSize, sortBy);
    }

    @GetMapping("/admin/listAllSellers")
    public List<RegisteredSellerDto> getAllSellers(@RequestParam(name = "pageNo", required = true, defaultValue = "0") Integer pageNo,
                                                   @RequestParam(name = "pageSize", required = true, defaultValue = "10") Integer pageSize,
                                                   @RequestParam(name = "sortBy", defaultValue = "id") String sortBy) {
        return adminService.getAllRegisteredSellers(pageNo, pageSize, sortBy);
    }


    @GetMapping("/admin/activate")
    public ResponseEntity activateCustomer(@RequestParam("id") Long id) {
        return userService.activateUser(id);
    }

    @GetMapping("/admin/deactivate")
    public ResponseEntity deactivateCustomer(@RequestParam("id") Long id) {
        return userService.deActivateuser(id);
    }

    @PutMapping("/admin/lock")
    public ResponseEntity lockUser(@RequestParam("id") Long id) {
        return adminService.lockUser(id);
    }

    @PutMapping("/admin/unlock")
    public ResponseEntity unlockUser(@RequestParam("id") Long id) {
        return adminService.unlockUser(id);
    }

    @GetMapping("/admin/userget")
    public UserEntity getProfile(@RequestParam("email") String email) {
        return criteria.findByEmail(email);
    }

}
*/
