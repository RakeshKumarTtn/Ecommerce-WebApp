package com.org.ecom.entity.product;

import com.org.ecom.entity.registration.Customer;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@Table(name = "PRODUCT_REVIEW")
public class ProductReview implements Serializable {

    @EmbeddedId
    private ProductReviewKey id;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "PRODUCT_ID")
    private Product product;

    @ManyToOne
    @MapsId("customerId")
    @JoinColumn(name = "CUSTOMER_USER_ID")
    private Customer customer;

    @Column(name = "REVIEW")
    private String review;

    @Column(name = "RATING")
    private int rating;
}
