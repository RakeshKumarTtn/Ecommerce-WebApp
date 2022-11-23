package com.org.ecom.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor

public class CustomResetPassword {

    String jwtToken;
    String username;
    String password;
    String confirmPassword;
}
