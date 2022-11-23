package com.org.ecom.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Set;

@NoArgsConstructor
@Setter
@Getter

public class UserDto {

    @Autowired
    private Set<AddressDto> addressDtoSet;

    @NotEmpty
    private String username;

    @NotEmpty(message = "Enter the firstName")
    private String firstName;

    @NotEmpty(message = "Enter the lastName")
    private String lastName;

    private String middleName;

    @Column(nullable = false, unique = true)
    @NotEmpty
    @Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}",
            flags = Pattern.Flag.CASE_INSENSITIVE)
    private String email;

    @NotEmpty(message = "Password cannot be null")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&-+=()])(?=\\\\S+$).{8,20}$")
    private String password;

    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&-+=()])(?=\\\\S+$).{8,20}$")
    private String confirmPassword;

    private String passwordUpdateDate;

    private int invalidAttemptCount;

    private Boolean isDeleted;

    private Boolean isActive;

    private Boolean isExpired;

    private Boolean isLocked;

    public UserDto(String email, String username, String firstName, String middleName, String lastName, String password, String confirmPassword) {
        this.email = email;
        this.username = username;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    public void addAddress(AddressDto address) {
        if (address != null) {
            address.setUserDto(this);
            addressDtoSet.add(address);
        }
    }
}
