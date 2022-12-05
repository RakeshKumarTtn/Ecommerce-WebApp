package com.org.ecom.entity.registration;


import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.org.ecom.entity.product.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "SELLER")
@Getter
@Setter
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@JsonFilter("SellersFilter")
public class Seller extends UserEntity {

    @Column(name = "GST", updatable = true, nullable = false)
    private String gstin;

    @Column(name = "COMPANY_NAME", updatable = true, nullable = false)
    private String companyName;

    @Column(name = "COMPANY_CONTACT", updatable = true, nullable = false)
    private String companyContact;

    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL)
    private Set<Product> products;

    public void addProduct(Product product) {
        if (product != null) {
            if (products == null) {
                products = new HashSet<Product>();
            }
            products.add(product);
            product.setSeller(this);
        }
    }
}