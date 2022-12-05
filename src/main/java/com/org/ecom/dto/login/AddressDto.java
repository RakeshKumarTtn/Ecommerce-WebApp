package com.org.ecom.dto.login;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class AddressDto {

    private String addressLine;
    private String cityName;
    private String stateName;

    @Size(min = 6, max = 6, message = "Zipcode should be of length 6")
    private Long zipCode;

    private String countryName;

    private String label;


    private UserDto userDto;

}
