package com.org.ecom.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileDto {
    private Long id;
    private String firstName;
    private String lastName;
    private Boolean isActive;
    private String image;
}