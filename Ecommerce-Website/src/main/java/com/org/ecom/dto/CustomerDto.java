package com.org.ecom.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

public class CustomerDto extends UserDto {

    private Long contact;

    public CustomerDto(String email, String username, String firstName, String middleName, String lastName, String password, String confirmPassword, AddressDto address, Long contact) {
        super(email, username, firstName, middleName, lastName, password, confirmPassword, address);
        this.contact = contact;
    }
}