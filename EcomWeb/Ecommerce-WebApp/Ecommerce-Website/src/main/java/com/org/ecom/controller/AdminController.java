package com.org.ecom.controller;

import com.org.ecom.dto.RegisteredCustomerDto;
import com.org.ecom.dto.RegisteredSellerDto;
import com.org.ecom.entities.UserEntity;
import com.org.ecom.repository.CustomerRepository;
import com.org.ecom.repository.SellerRepository;
import com.org.ecom.service.AdminService;
import com.org.ecom.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class AdminController {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private AdminService adminService;

    @GetMapping("/admin/home")
    public ResponseEntity adminHome() {
        String msg = "Admin home";
        return new ResponseEntity(msg, HttpStatus.OK);
    }

    @GetMapping("/admin/user_profile")
    public UserEntity getProfile(@RequestParam("email") String email) {
        return userService.getProfile(email);
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

    @PutMapping("/admin/activate_customer")
    public ResponseEntity activateCustomer(@RequestParam("id") Long id) {
        return userService.activateCustomer(id);
    }

    @PutMapping("/admin/deactivate_customer")
    public ResponseEntity deactivateCustomer(@RequestParam("id") Long id) {
        return userService.deactivateCustomer(id);
    }

    @PutMapping("/admin/activate_seller")
    public ResponseEntity activateSeller(@RequestParam("id") Long id) {
        return userService.activateSeller(id);
    }

    @PutMapping("/admin/deactivate_seller")
    public ResponseEntity deactivateSeller(@RequestParam("id") Long id) {
        return userService.deactivateSeller(id);
    }

    @PutMapping("/admin/lock_user")
    public ResponseEntity lockUser(@RequestParam("id") Long id) {
        return adminService.lockUser(id);
    }

    @PutMapping("/admin/unlock_user")
    public ResponseEntity unlockUser(@RequestParam("id") Long id) {
        return adminService.unlockUser(id);
    }


    /*
    @PatchMapping(path = "/{id}", consumes = PatchMediaType.APPLICATION_JSON_PATCH_VALUE)
    public ResponseEntity<Void> updateContact(@PathVariable Long id,
                                              @RequestBody JsonPatch patchDocument) {

        Contact contact = service.findContact(id).orElseThrow(ResourceNotFoundException::new);
        ContactResourceInput contactResource = mapper.asInput(contact);
        ContactResourceInput contactResourcePatched = patchHelper.patch(patchDocument, contactResource, ContactResourceInput.class);

        mapper.update(contact, contactResourcePatched);
        service.updateContact(contact);

        return ResponseEntity.noContent().build();
    }


    */
}