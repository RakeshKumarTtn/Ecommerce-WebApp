package com.org.ecom.dto.login;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor

public class CustomerDto extends UserDto {

    @Column(unique = true)
    @Pattern(regexp="^\\d{10}$",
    message = "Phone number is not valid")
    private String contact;

    public CustomerDto(String email, String username, String firstName, String middleName, String lastName, String password, String confirmPassword, String contact) {
        super(email, username, firstName, middleName, lastName, password, confirmPassword);
        this.contact = contact;
    }
}