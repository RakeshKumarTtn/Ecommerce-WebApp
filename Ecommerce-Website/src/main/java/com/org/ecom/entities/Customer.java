package com.org.ecom.entities;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "CUSTOMER")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class Customer extends User {

    @Column(name = "COMPANY_CONTACT", updatable = true, nullable = false)
    private String contact;
}
