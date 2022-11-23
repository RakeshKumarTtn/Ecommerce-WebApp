package com.org.ecom.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.Column;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class AddressDto {

    private String addressLine;
    private String city;
    private String state;

    //@Size(min = 6, max = 6, message = "Zipcode should be of length 6")
    private Long zipCode;
    private String country;
    private String label;

//    @Autowired
    private UserDto userDto;

}
