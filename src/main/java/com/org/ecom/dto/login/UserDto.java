package com.org.ecom.dto.login;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Set;

@NoArgsConstructor
@Setter
@Getter

public class UserDto {

    private Set<AddressDto> addressDtoSet;

    @NotEmpty(message = "Username cannot be empty")
    private String username;

    @Size(min = 2, max = 10, message = "First name should be greater than 2 characters")
    @NotEmpty(message = "Enter the firstName")
    private String firstName;

    @NotEmpty(message = "Enter the lastName")
    private String lastName;

    private String middleName;

    @Column(nullable = false, unique = true)
    @NotEmpty
    @Email(regexp = "[a-z0-9._%+-]+@[a-z.-]+\\.[a-z]{2,3}",
            flags = Pattern.Flag.CASE_INSENSITIVE,
            message = "Email is not valid")
    private String email;

    @NotEmpty(message = "Password cannot be null")
    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$",
            message = "Password is not valid")
    private String password;

    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$",
            message = "Confirm Password is not valid")
    private String confirmPassword;

    private String passwordUpdateDate;

    private int invalidAttemptCount;

    private Boolean isDeleted;

    private Boolean isActive;

    private Boolean isExpired;

    private Boolean isLocked;

    @Transient
    private String imageFilePath;

    public UserDto(String email, String username, String firstName, String middleName, String lastName,
                   String password, String confirmPassword) {
        this.email = email;
        this.username = username;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }
}
