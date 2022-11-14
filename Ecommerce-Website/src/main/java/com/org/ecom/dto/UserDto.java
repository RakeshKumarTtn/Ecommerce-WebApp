package com.org.ecom.dto;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Setter
@Getter
public class UserDto {

    private Long userId;

    @NotEmpty
    private String username;

    @NotEmpty(message = "Enter the firstName")
    private String firstName;

    @NotEmpty(message = "Enter the lastName")
    private String lastName;

    private String middleName;

    @Column(nullable = false, unique = true)
    @NotEmpty
    @Email(message = "Invalid Email")
    private String email;

    @NotEmpty(message = "Password cannot be null")
    @Size(min = 8)
    private String password;

    private String confirmPassword;

    private String passwordUpdateDate;

    private int invalidAttemptCount;

    private Boolean isDeleted;

    private Boolean isActive;

    private Boolean isExpired;

    private Boolean isLocked;
}
