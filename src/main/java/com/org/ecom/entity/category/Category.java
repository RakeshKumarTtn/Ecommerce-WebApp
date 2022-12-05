package com.org.ecom.entity.category;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.org.ecom.entity.product.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CATEGORY")
public class Category implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CATEGORY_ID")
    private Long id;

    @Column(name = "NAME",unique = true)
    private String name;

    @OneToOne
    @JoinColumn(name = "PARENT_ID")
    private Category category;

    @JsonIgnore
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private Set<Product> products;

    public void addProduct(Product product) {
        if (product != null) {
            if (products == null) {
                products = new HashSet<Product>();
            }
            products.add(product);
            product.setCategory(this);
        }
    }
}
