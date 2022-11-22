package com.org.ecom.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDto extends UserDto {

    private Long contact;

    public CustomerDto(String email, String username, String firstName, String middleName, String lastName, String password, String confirmPassword, Long contact) {
        super(email, username, firstName, middleName, lastName, password, confirmPassword);
        this.contact = contact;
    }
}