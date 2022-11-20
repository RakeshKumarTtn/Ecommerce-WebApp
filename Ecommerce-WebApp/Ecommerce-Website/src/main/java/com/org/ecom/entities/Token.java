package com.org.ecom.entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class Token {

    @Id
    private Integer id;
    private String email;
    private String token;


}
