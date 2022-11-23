package com.org.ecom.service;


import com.org.ecom.constant.APPConstant;
import com.org.ecom.dto.AddressDto;
import com.org.ecom.dto.CustomerDto;
import com.org.ecom.dto.SellerDto;
import com.org.ecom.entities.*;
import com.org.ecom.repository.CustomUsersRepository;
import com.org.ecom.repository.RoleRepository;
import com.org.ecom.security.*;
import com.org.ecom.utility.CustomJwtUtility;
import com.org.ecom.utility.ObjectConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.org.ecom.enums.RoleLevel.CUSTOMER;
import static com.org.ecom.enums.RoleLevel.SELLER;


@Service
public class CustomUserService implements UserDetailsService {

    @Autowired
    private BlackListedTokensService blackListedTokensService;

    @Autowired
    private CustomJwtUtility jwtUtility;

    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    private CustomUsersRepository customUsersRepository;

    @Autowired
    private CustomizedAuditorAware customizedAuditorAware;

    @Autowired
    private ObjectConverter objectConverter;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserEntity byUsername = customUsersRepository.findByUsernameAndIsActiveAndIsLockedAndIsExpired(username, true, false, false)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));


        return new User(byUsername.getUsername(), byUsername.getPassword(),
                mapRolesToAuthorities(byUsername.getRoles()));
    }

    private Collection<GrantedAuthority> mapRolesToAuthorities(Set<Role> rolesSet) {
        return rolesSet.stream().map(role -> new SimpleGrantedAuthority(role.getAuthority())).collect(Collectors.toSet());
    }

    public void registerSeller(SellerDto sellerDto) {

        customizedAuditorAware.setName(sellerDto.getFirstName());

        Seller seller = objectConverter.dtoToEntity(sellerDto);

        Optional<Role> byAuthority = roleRepository.findByAuthority(String.valueOf(SELLER));

        byAuthority.ifPresent(seller::addRole);

        String resetToken = jwtUtility.generateResetToken(seller);


        for (AddressDto addressDto : sellerDto.getAddressDtoSet()) {
            Address address = objectConverter.dtoToEntity(addressDto);
            seller.addAddress(address);
        }

        emailSenderService.sendEmail(seller.getEmail(),
                "Regarding Account activation.",
                "Activate your account using this link within 24 hours.\n" + resetToken);


        customUsersRepository.save(seller);
    }

    public void registerCustomer(CustomerDto customerDto) {
        customizedAuditorAware.setName(customerDto.getFirstName());

        Customer customer = objectConverter.dtoToEntity(customerDto);

        Optional<Role> byAuthority = roleRepository.findByAuthority(String.valueOf(CUSTOMER));

        byAuthority.ifPresent(customer::addRole);


        String resetToken = jwtUtility.generateResetToken(customer);

        emailSenderService.sendEmail(customer.getEmail(),
                "Regarding Account activation.",
                "Activate your account using this link within 24 hours.\n" + resetToken);

//        for(AddressDto addressDto: customerDto.getAddressDtoSet()) {
//            Address address = objectConverter.dtoToEntity(addressDto);
//            customer.addAddress(address);
//        }

        customUsersRepository.save(customer);

    }

    public UserEntity loadUserByUsernameAndEmail(String username, String email) {
        UserEntity findByUsernameAndEmail = customUsersRepository.findByUsernameAndEmail(username, email)
                .orElseThrow(() -> new UsernameNotFoundException("Username or Email not found"));

        return findByUsernameAndEmail;
    }

    public ResponseEntity<String> resetPassword(CustomResetPassword customResetPassword) {
        String jwtToken = customResetPassword.getJwtToken();
        Date diff = new Date(jwtUtility.extractExpiration(jwtToken).getTime()
                - jwtUtility.extractIssuedAt(jwtToken).getTime());

        Date date = new Date(1000 * 60 * 5);

        if (diff.getTime() == date.getTime()) {
            String username = jwtUtility.extractUsername(jwtToken);

            UserEntity byUsername = customUsersRepository.findByUsernameAndIsActiveAndIsLockedAndIsExpired(username, true, false, false)
                    .orElseThrow(() -> new UsernameNotFoundException("Username not found"));

            String encode = passwordEncoder.encode(customResetPassword.getPassword());
            byUsername.setPassword(encode);

            customizedAuditorAware.setName(byUsername.getFirstName());
            customUsersRepository.save(byUsername);

            byUsername.setPasswordUpdateDate(new Date());

            String emailBody = String.valueOf(APPConstant.AFTER_RESET_EMAIL_MESSAGE.insert(6, byUsername.getUsername()));
            String emailSubject = "[Ecommerce] Your password was reset";
            emailSenderService.sendEmail(byUsername.getEmail(), emailSubject, emailBody);
            int usernameLength = byUsername.getUsername().length();
            APPConstant.BEFORE_RESET_EMAIL_MESSAGE.replace(6, 6 + usernameLength, "");
            emailBody = null;
            return ResponseEntity.ok("Password is successfully updated");
        }
        return ResponseEntity.of(Optional.of("Reset token has expired"));
    }


    public ResponseEntity<String> forgotPassword(CustomMailRequest customMailRequest) {

        UserEntity user = loadUserByUsernameAndEmail(customMailRequest.getUsername(), customMailRequest.getEmail());

        String resetToken =
                this.jwtUtility.generateResetToken(user);

        String sendToken = new CustomJWTResponse(resetToken).getJwtToken();
        String emailBody = String.valueOf(APPConstant.BEFORE_RESET_EMAIL_MESSAGE.insert(182, "\n\n" + sendToken));
        String emailSubject = "[Ecommerce] Please reset your password";
        emailSenderService.sendEmail(user.getEmail(), emailSubject, emailBody);
        int tokenSize = sendToken.length();
        APPConstant.BEFORE_RESET_EMAIL_MESSAGE.replace(182, 182 + tokenSize, "");
        emailBody = null;
        return ResponseEntity.ok("Reset Token sent on email.");
    }

    public String logout(CustomJWTResponse token) {
        Date diff = new Date(jwtUtility.extractExpiration(token.getJwtToken()).getTime() - jwtUtility.extractIssuedAt(token.getJwtToken()).getTime());

        Date date = new Date(1000 * 60 * 10);

        if (diff.getTime() == date.getTime()) {
            BlackListedTokens blackListedTokens = new BlackListedTokens();
            blackListedTokens.setJwtToken(token.getJwtToken());
            blackListedTokensService.saveToken(blackListedTokens);
            return "Logout Successful";
        }
        throw new RuntimeException("Provide valid Access Token");
    }

    public ResponseEntity<CustomJWTResponse> generate_access_token(CustomJWTResponse customJWTResponse) {
        String jwtToken = customJWTResponse.getJwtToken();

        Date diff = new Date(jwtUtility.extractExpiration(jwtToken).getTime() - jwtUtility.extractIssuedAt(jwtToken).getTime());

        Date date = new Date(1000 * 60 * 60 * 24);

        if (diff.getTime() == date.getTime()) {
            if (jwtUtility.extractExpiration(jwtToken).after(new Date(System.currentTimeMillis()))) {
                String username = jwtUtility.extractUsername(jwtToken);

                UserDetails userDetails = loadUserByUsername(username);

                String access_token =
                        this.jwtUtility.generateAccessToken(userDetails);

                return ResponseEntity.ok(new CustomJWTResponse(access_token));
            } else {
                throw new RuntimeException("Refresh Token expired. Generate new Refresh Token and try again");
            }
        } else {
            throw new RuntimeException("Provide a valid Refresh Token");
        }
    }

    public ResponseEntity<Map<String, CustomJWTResponse>> login(CustomJWTRequest jwtRequest) {
        UserDetails userDetails = loadUserByUsername(jwtRequest.getUsername());

        String access_token =
                this.jwtUtility.generateAccessToken(userDetails);

        String refresh_token =
                this.jwtUtility.generateRefreshToken(userDetails);

        Map<String, CustomJWTResponse> map = new Hashtable();

        map.put("access token", new CustomJWTResponse(access_token));
        map.put("refresh token", new CustomJWTResponse(refresh_token));

        return ResponseEntity.ok(map);
    }
}
