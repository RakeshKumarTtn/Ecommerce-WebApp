package com.org.ecom.controller;

import com.org.ecom.dto.security.CustomJWTRequestDto;
import com.org.ecom.dto.security.CustomJWTResponseDto;
import com.org.ecom.dto.security.CustomMailRequestDto;
import com.org.ecom.dto.security.CustomResetPasswordDto;
import com.org.ecom.exception.InvalidUsernameOrPasswordException;
import com.org.ecom.exception.LockedException;
import com.org.ecom.payload.FileResource;
import com.org.ecom.service.CustomUserService;
import com.org.ecom.service.FileService;
import com.org.ecom.service.UserService;
import com.org.ecom.utility.CustomJwtUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.activation.ActivationException;
import java.util.Map;


@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private CustomJwtUtility jwtUtility;

    @Autowired
    private CustomUserService customUserService;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomMailRequestDto customMailRequest;

    @Autowired
    FileService fileService;

    @Autowired
    CustomJwtUtility customJwtUtility;

    @Autowired
    MessageSource messageSource;

    @Value("${project.image}")
    public String path;

    //Login API for Admin,Sellers AND Customers
    @PostMapping(value = "/login")
    public ResponseEntity<Map<String, CustomJWTResponseDto>> loginUser(@Valid @RequestBody CustomJWTRequestDto jwtRequest) throws ActivationException {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            jwtRequest.getUsername(),
                            jwtRequest.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            if (!userService.locked(jwtRequest.getUsername())) {
                throw new LockedException(messageSource.getMessage("message56.txt", null, LocaleContextHolder.getLocale()));
            }
            throw new InvalidUsernameOrPasswordException(messageSource.getMessage("message57.txt", null, LocaleContextHolder.getLocale()));
        }
        return customUserService.login(jwtRequest);
    }


    //Generate_Access_Token API for generating the new Access Token by the help of Refresh Token
    @PostMapping("/generate_access_token")
    public ResponseEntity<CustomJWTResponseDto> generateAccessToken(@Valid @RequestBody CustomJWTResponseDto customJWTResponse) {
        return customUserService.generate_access_token(customJWTResponse);
    }

    //Logout API for logging out the Admin, Customers and Sellers
    @PutMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody CustomJWTResponseDto customJWTResponse) {
        return customUserService.logout(customJWTResponse);
    }

    //Forget_Password API for forget the password of User
    @PostMapping("/forgot_password")
    public ResponseEntity<String> sendMail(@Valid @RequestBody CustomMailRequestDto customMailRequest) throws ActivationException {
        return customUserService.forgotPassword(customMailRequest);
    }

    //Reset_Password API for reset the password
    @PutMapping("/reset_password")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody CustomResetPasswordDto customResetPassword) {
        return customUserService.resetPassword(customResetPassword);
    }

    //Upload_Image API for Admin, Sellers and Customers
    @PostMapping(value = "/upload_image")
    public ResponseEntity<FileResource> fileUpload(@RequestParam("jwtToken") CustomJWTResponseDto jwtToken,
                                                   @RequestParam("image") MultipartFile image) throws IOException {
        String userName = customJwtUtility.extractUsername(jwtToken.getJwtToken());
        String fileName = null;
        try {
            fileName = this.fileService.uploadImage(userName, path, image);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(new FileResource(null, "Image is not uploaded Dua to Some Error on server !!"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new FileResource(fileName, "Image Successfully Uploaded !!"), HttpStatus.OK);
    }

    //Get_Image API for getting the image of LoggedInUser when we hit API from the postman
    @GetMapping(value = "/get_image", produces = MediaType.IMAGE_PNG_VALUE)
    public void getUserImage(@RequestParam("jwtToken") CustomJWTResponseDto jwtToken, HttpServletResponse response) throws IOException {
        String userName = customJwtUtility.extractUsername(jwtToken.getJwtToken());
        InputStream resource = fileService.getUserImage(path, userName + ".png");
        response.setContentType(MediaType.IMAGE_PNG_VALUE);
        StreamUtils.copy(resource, response.getOutputStream());
    }
}