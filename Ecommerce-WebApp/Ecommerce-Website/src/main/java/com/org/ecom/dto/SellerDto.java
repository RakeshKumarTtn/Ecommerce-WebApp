package com.org.ecom.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;

@Setter
@Getter
@NoArgsConstructor
public class SellerDto extends UserDto {

    @Column(unique = true)
    private String gstin;

    @Column(unique = true)
    private Long companyContact;

    @Column(unique = true)
    private String companyName;

    public SellerDto(String email, String username, String firstName, String middleName, String lastName, String password, String confirmPassword, String gst, Long companyContact, String companyName) {
        super(email, username, firstName, middleName, lastName, password, confirmPassword);
        this.gstin = gst;
        this.companyContact = companyContact;
        this.companyName = companyName;
    }

    public SellerDto(String email, String username, String firstName, String middleName, String lastName, String password, String confirmPassword) {
        super(email, username, firstName, middleName, lastName, password, confirmPassword);
    }
}