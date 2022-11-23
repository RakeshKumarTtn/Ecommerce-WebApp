package com.org.ecom.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Size;

@Setter
@Getter
@NoArgsConstructor
public class CustomerProfileDto extends UserProfileDto {
    @Size(min = 10, max = 10, message = "invalid phone number")
    private Long contact;

    public CustomerProfileDto(@Size(min = 10, max = 10, message = "Contact number invalid") Long contact) {
        this.contact = contact;
    }
}