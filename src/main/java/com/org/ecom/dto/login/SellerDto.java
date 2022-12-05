package com.org.ecom.dto.login;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.Pattern;

@Setter
@Getter
@NoArgsConstructor

public class SellerDto extends UserDto {

    @Column(unique = true)
    private String gstin;


    @Column(unique = true)
    @Pattern(regexp="^\\d{10}$",
             message = "Phone number is not valid")
    private String companyContact;


    @Column(unique = true)
//    @Pattern(regexp = "", flags = Pattern.Flag.CASE_INSENSITIVE)
    private String companyName;


    public SellerDto(String email, String username, String firstName, String middleName, String lastName, String password, String confirmPassword, String gstin, String companyContact, String companyName) {
        super(email, username, firstName, middleName, lastName, password, confirmPassword);
        this.gstin = gstin;
        this.companyContact = companyContact;
        this.companyName = companyName;
    }
}