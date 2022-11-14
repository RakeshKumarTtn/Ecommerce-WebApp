package com.org.ecom.entities;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "SELLER")
@Getter
@Setter
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Seller extends User {

    @Column(name = "GST", updatable = true, nullable = false)
    private String contact;

    @Column(name = "COMPANY_NAME", updatable = true, nullable = false)
    private String companyName;

    @Column(name = "COMPANY_CONTACT", updatable = true, nullable = false)
    private String companyContact;
}