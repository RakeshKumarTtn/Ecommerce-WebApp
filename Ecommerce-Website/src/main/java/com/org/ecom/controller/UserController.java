package com.org.ecom.controller;

import com.org.ecom.security.CustomJWTRequest;
import com.org.ecom.security.CustomJWTResponse;
import com.org.ecom.security.CustomMailRequest;
import com.org.ecom.security.CustomResetPassword;
import com.org.ecom.service.CustomUserService;
import com.org.ecom.utility.CustomJwtUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/api/v1/user")

public class UserController {

    @Autowired
    private CustomJwtUtility jwtUtility;

    @Autowired
    private CustomUserService customUserService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomMailRequest customMailRequest;


    @PostMapping("/login")
    public ResponseEntity<Map<String, CustomJWTResponse>> login(@RequestBody CustomJWTRequest jwtRequest) throws Exception {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            jwtRequest.getUsername(),
                            jwtRequest.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new Exception("Bad Credentials");
        }
        return customUserService.login(jwtRequest);
    }


    @PostMapping("/generate_access_token")
    public ResponseEntity<CustomJWTResponse> generateAccessToken(@RequestBody CustomJWTResponse customJWTResponse) {
        return customUserService.generate_access_token(customJWTResponse);
    }

    @GetMapping("/logout/{token}")
    public String logout(@PathVariable("token") String token) {
        return customUserService.logout(token);
    }

    @PostMapping("/forgot_password")
    public ResponseEntity<String> sendMail(@RequestBody CustomMailRequest customMailRequest) {
        return customUserService.forgotPassword(customMailRequest);
    }

    @PutMapping("/reset_password")
    public ResponseEntity<String> resetPassword(@RequestBody CustomResetPassword customResetPassword) {
        return customUserService.resetPassword(customResetPassword);
    }
}