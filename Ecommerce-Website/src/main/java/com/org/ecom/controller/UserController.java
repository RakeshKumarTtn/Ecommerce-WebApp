package com.org.ecom.controller;

import com.org.ecom.dto.UserDto;
import com.org.ecom.entities.User;
import com.org.ecom.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/ecom/user")
public class UserController {

    @Autowired
    UserService userService;

    /*@PostMapping()
    public UserDto addUser(@RequestBody UserDto userDto) {
        return userService.addUser(userDto);
    }

    @GetMapping("/active")
    public Set<User> getActiveUser() {
        return userService.getActiveUser();
    }

    @PutMapping("update")
    public UserDto updateUser(@RequestBody UserDto userDto) {
        return userService.updateUser(userDto);
    }

    @DeleteMapping("/delete/{email}")
    public User deleteUserByEmail(@PathVariable String email) {
        return userService.deleteUserByEmail(email);
    }

    @DeleteMapping("/delete/username/{username}")
    public User deleteUserByUserName(@PathVariable String username) {
        return userService.deleteUserByUserName(username);
    }*/

/*
    @PatchMapping(path = "/update")
    @ResponseBody
    public ResponseEntity updateUserPatch(@RequestBody UserDto userDto) {
        return userService.updateUserPatch(userDto);
    }*/
}
