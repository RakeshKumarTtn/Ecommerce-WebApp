package com.org.ecom.entity.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.org.ecom.utility.HashMapConverter;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Getter
@Setter
@Table(name = "PRODUCT_VARIATION")
@EntityListeners(AuditingEntityListener.class)
public class ProductVariation implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private long id;

    @Min(value = 0, message = "Quantity should be greater than 0")
    @NotNull
    @Column(name = "QUANTITY_AVAILABLE")
    private int quantityAvailable;

    @Min(value = 0, message = "Price should be greater than 0")
    @NotNull
    @Column(name = "PRICE")
    private double price;

    // how to store json?
    @Column(name = "METADATA", columnDefinition = "JSON")
    private String metadata;

    //String or byte[] ?
    @Column(name = "PRIMARY_IMAGE_NAME")
    private String primaryImageName;

    @JsonProperty
    @Column(name = "IS_ACTIVE")
    private boolean isActive;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "PRODUCT_ID")
    private Product product;


    private String infoJson;

    @CreatedBy
    @Column(name = "CREATED_BY", nullable = false, updatable = false)
    private String createdBy;

    @CreatedDate
    @Column(name = "CREATED_DATE", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedBy
    @Column(name = "LAST_MODIFIED_BY", nullable = false)
    private String lastModifiedBy;

    @LastModifiedDate
    @Column(name = "LAST_MODIFIED_DATE", nullable = false)
    private LocalDateTime lastModifiedDate;

    @Convert(converter = HashMapConverter.class)
    private Map<String, Object> infoAttributes;
}