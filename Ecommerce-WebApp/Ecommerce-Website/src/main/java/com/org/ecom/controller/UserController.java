package com.org.ecom.controller;

import com.org.ecom.dto.AuthResponseDTO;
import com.org.ecom.dto.LoginDTO;
import com.org.ecom.dto.RegisterDTO;
import com.org.ecom.entities.Role;
import com.org.ecom.entities.Seller;
import com.org.ecom.entities.UserEntity;
import com.org.ecom.repository.RolesRepository;
import com.org.ecom.repository.SellerRepository;
import com.org.ecom.repository.UserRepository;
import com.org.ecom.security.CustomJWTRequest;
import com.org.ecom.security.CustomJWTResponse;
import com.org.ecom.service.CustomUserService;
import com.org.ecom.service.EmailSenderService;
import com.org.ecom.service.UserService;
import com.org.ecom.utility.CustomJwtUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@RestController
@RequestMapping("/api/v1/ecom/user")
//@PreAuthorize("hasRole('SELLER') or hasRole('CUSTOMER')")
public class UserController {


    @Autowired
    private CustomUserService customUserService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RolesRepository rolesRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private CustomJwtUtility jwtGenerator;

    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    UserService userService;

    //register
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterDTO registerDto) {
        if (userRepository.existsByUsername(registerDto.getUsername())) {
            return new ResponseEntity<>("Username is taken!", HttpStatus.BAD_REQUEST);
        }

        Seller user = new Seller();
//      UserEntity user = new UserEntity();
        user.setUsername(registerDto.getUsername());
        user.setPassword(passwordEncoder.encode((registerDto.getPassword())));

        Role roles = rolesRepository.findByUserEntities("SELLER").get();
        user.setRoles(Collections.singleton(roles));

        sellerRepository.save(user);
        String ID = UUID.randomUUID().toString();
//      "http://localhost:8080/seller/activate/"

        String URL = "http://localhost:8080/api/v1/ecom/customer/register" + ID;

        userService.saveUUIDTokenWithEmail(registerDto.getUsername(), ID);

        emailSenderService.sendEmail(registerDto.getUsername(), "HELLO SELLER ", URL);

        return new ResponseEntity<>("User registered success!", HttpStatus.OK);
    }

    //login
/*    @PostMapping("login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginDTO loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getUsername(),
                        loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtGenerator.generateAccessToken((UserDetails) authentication);
        return new ResponseEntity<>(new AuthResponseDTO(token), HttpStatus.OK);
    }*/

    @GetMapping("/getUser")
    public String getUser() {
        return "User";
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
        UserDetails userDetails = this.customUserService.loadUserByUsername(jwtRequest.getUsername());

        String access_token =
                this.jwtGenerator.generateAccessToken(userDetails);


        String refresh_token =
                this.jwtGenerator.generateRefreshToken(userDetails);


        Map<String, CustomJWTResponse> map = new HashMap();

        map.put("access token", new CustomJWTResponse(access_token));
        map.put("refresh token", new CustomJWTResponse(refresh_token));

        return ResponseEntity.ok(map);
    }


    @PostMapping("/generate_access_token")
    public ResponseEntity<CustomJWTResponse> authenticate3(@RequestBody CustomJWTResponse customJWTResponse) throws Exception {

        String jwtToken = customJWTResponse.getJwtToken();

        Date diff = new Date(jwtGenerator.extractExpiration(jwtToken).getTime() - jwtGenerator.extractIssuedAt(jwtToken).getTime());

        Date date = new Date(1000 * 60 * 60 * 24);

        if (diff.getTime() == date.getTime()) {
            if (jwtGenerator.extractExpiration(jwtToken).after(new Date(System.currentTimeMillis()))) {
                String username = jwtGenerator.extractUsername(jwtToken);

                UserDetails userDetails = this.customUserService.loadUserByUsername(username);

                String access_token =
                        this.jwtGenerator.generateAccessToken(userDetails);

                return ResponseEntity.ok(new CustomJWTResponse(access_token));
            } else {
                throw new Exception("Refresh Token Expired. Generate Refresh Token again and then try");
            }
        } else {
            throw new Exception("Please enter Refresh Token");
        }

    }

//    @GetMapping("/logout")
//    public String logout(){
//        SecurityContextHolder.getContext().setAuthentication(null);
//        SecurityContextHolder.clearContext();
//        return "Logout successful";
//    }
}