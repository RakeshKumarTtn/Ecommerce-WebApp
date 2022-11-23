package com.org.ecom.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Size;

@Setter
@Getter
@NoArgsConstructor
public class SellerProfileDto extends UserProfileDto {

    @Size(min = 15, max = 15)
    private String GST;

    private String companyName;

    @Size(min = 10, max = 10)
    private Long companyContact;

    private AddressDto addressDto;

    public SellerProfileDto(Long id, String firstName, String lastName, Boolean isActive, String image, String GST, Long companyContact, String companyName, AddressDto addressDto) {
        super(id, firstName, lastName, isActive, image);
        this.GST = GST;
        this.companyContact = companyContact;
        this.companyName = companyName;
        this.addressDto = addressDto;
    }
}
