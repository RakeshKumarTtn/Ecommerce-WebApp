package com.org.ecom.entity.registration;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ADDRESS")

public class Address implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer addressId;

//    @Pattern(regexp = "^[A-Za-z ]{1,32}$")
    @Column(name = "CITY")
    private String cityName;

//    @Pattern(regexp = "^[A-Za-z ]{1,32}$")
    @Column(name = "STATE")
    private String stateName;

//    @Pattern(regexp = "^[A-Za-z ]{1,32}$")
    @Column(name = "COUNTRY")
    private String countryName;

    @Column(name = "ADDRESS_LINE")
    private String addressLine;

//    @Pattern(regexp = "^[0-9]{5}$")
    @Column(name = "ZIP_CODE")
    private String zipCode;

    @Column(name = "LABEL")
    private String label;


    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    private UserEntity user;
}
