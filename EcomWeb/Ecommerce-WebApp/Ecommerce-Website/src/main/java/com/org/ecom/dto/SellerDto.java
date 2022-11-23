package com.org.ecom.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.Pattern;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor

public class SellerDto extends UserDto {

    @Column(unique = true)
    private String gstin;

    @Column(unique = true)
    @Pattern(regexp="@\"^[0-9]{10}$\"")
    private Long companyContact;

    @Column(unique = true)
    private String companyName;

    public SellerDto(String email, String username, String firstName, String middleName, String lastName, String password, String confirmPassword, Set<AddressDto> address, String gstin, Long companyContact, String companyName) {
        super(email, username, firstName, middleName, lastName, password, confirmPassword);
        this.gstin = gstin;
        this.companyContact = companyContact;
        this.companyName = companyName;
    }
}