package com.org.ecom.controller;

import com.org.ecom.entities.UserEntity;
import com.org.ecom.security.CustomJWTRequest;
import com.org.ecom.security.CustomJWTResponse;
import com.org.ecom.service.CustomUserService;
import com.org.ecom.utility.CustomJwtUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api/v1/ecom/user")
//@PreAuthorize("hasRole('SELLER') or hasRole('CUSTOMER')")
public class UserController {

    @Autowired
    private CustomJwtUtility jwtUtility;

    @Autowired
    private CustomUserService customUserService;

    @Autowired
    private AuthenticationManager authenticationManager;


    @GetMapping("/getUser")
    public String getUser(){
        return "User";
    }


    @PostMapping("/register")
    public String register(@RequestBody UserEntity user){
        customUserService.save(user);
        return "Registration successful";
    }

//    @PostMapping("/login")
//    public ResponseEntity<CustomJWTResponse> authenticate1(@RequestBody CustomJWTRequest jwtRequest) throws Exception {
//
//        try
//         {
//            authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(
//                            jwtRequest.getUsername(),
//                            jwtRequest.getPassword()
//                    )
//            );
//        }
//
//        catch (BadCredentialsException e){
//            throw new Exception("Bad Credentials");
//        }
//            UserDetails userDetails = this.customUserService.loadUserByUsername(jwtRequest.getUsername());
//
//             String access_token =
//                    this.jwtUtility.generateAccessToken(userDetails);
//
//
//
//            return ResponseEntity.ok(new CustomJWTResponse(access_token));
//    }



    @PostMapping("/login")
    public ResponseEntity<Map<String, CustomJWTResponse>> authenticate1(@RequestBody CustomJWTRequest jwtRequest) throws Exception {

        try
        {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            jwtRequest.getUsername(),
                            jwtRequest.getPassword()
                    )
            );
        }

        catch (BadCredentialsException e){
            throw new Exception("Bad Credentials");
        }
        UserDetails userDetails = this.customUserService.loadUserByUsername(jwtRequest.getUsername());

        String access_token =
                this.jwtUtility.generateAccessToken(userDetails);


        String refresh_token =
                this.jwtUtility.generateRefreshToken(userDetails);


        Map<String,CustomJWTResponse> map = new HashMap();

        map.put("access token",new CustomJWTResponse(access_token));
        map.put("refresh token",new CustomJWTResponse(refresh_token));

        return ResponseEntity.ok(map);
    }



    @PostMapping("/generate_access_token")
    public ResponseEntity<CustomJWTResponse> authenticate3(@RequestBody CustomJWTResponse customJWTResponse) throws Exception {

        String jwtToken = customJWTResponse.getJwtToken();

        Date diff = new Date(jwtUtility.extractExpiration(jwtToken).getTime() - jwtUtility.extractIssuedAt(jwtToken).getTime());

        Date date = new Date(1000 * 60 * 3);

        if (diff.getTime() - date.getTime() > 0) {
            if (jwtUtility.extractExpiration(jwtToken).after(new Date(System.currentTimeMillis()))) {
                String username = jwtUtility.extractUsername(jwtToken);

                UserDetails userDetails = this.customUserService.loadUserByUsername(username);

                String access_token =
                        this.jwtUtility.generateAccessToken(userDetails);

                return ResponseEntity.ok(new CustomJWTResponse(access_token));
            } else {
                throw new Exception("Token Expired. Generate Refresh Token again and then try");
            }
        }
        else{
            throw new Exception("Refresh Token Expired. Generate Refresh Token again and then try");
        }

    }

//    @GetMapping("/logout")
//    public String logout(){
//        SecurityContextHolder.getContext().setAuthentication(null);
//        SecurityContextHolder.clearContext();
//        return "Logout successful";
//    }
}