package com.org.ecom.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisteredCustomerDto {
    Long id;
    String firstName;
    String middleName;
    String lastName;
    String username;
    Boolean isActive;
}