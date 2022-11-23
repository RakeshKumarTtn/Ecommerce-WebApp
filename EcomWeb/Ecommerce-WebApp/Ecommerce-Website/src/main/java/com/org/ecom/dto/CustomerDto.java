package com.org.ecom.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.Pattern;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor

public class CustomerDto extends UserDto {

    @Column(unique = true)
    @Pattern(regexp="@\"^[0-9]{10}$\"")
    private Long contact;

    public CustomerDto(String email, String username, String firstName, String middleName, String lastName, String password, String confirmPassword, Set<AddressDto> address, Long contact) {
        super(email, username, firstName, middleName, lastName, password, confirmPassword);
        this.contact = contact;
    }
}