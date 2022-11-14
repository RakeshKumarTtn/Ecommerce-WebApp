package com.org.ecom.controller;

import com.org.ecom.dto.UserDto;
import com.org.ecom.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ecom/user")
public class UserController {

    @Autowired
    UserService userService;


    @PostMapping()
    public UserDto addUser(@RequestBody UserDto user) {
        return userService.addUser(user);
    }

    @GetMapping("/active")
    public List<com.org.ecom.entities.User> getActiveUser() {
        return userService.getActiveUser();
    }

    @GetMapping("/{id}")
    public void getUser(@PathVariable int id) {

    }

    @GetMapping("/{name}")
    public void getUserByName(@PathVariable String name) {

    }

    @DeleteMapping()
    public void deleteUser() {

    }

    @PutMapping()
    public void updateUser() {

    }
}
