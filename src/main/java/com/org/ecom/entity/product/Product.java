package com.org.ecom.entity.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.org.ecom.entity.category.Category;
import com.org.ecom.entity.registration.Seller;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Table(name = "PRODUCT")
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Product implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private long ID;

    @NotNull
    @NotEmpty
    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @JsonProperty
    @Column(name = "IS_CANCELLABLE")
    private Boolean isCancellable;

    @JsonProperty
    @Column(name = "Is_RETURNABLE")
    public Boolean isReturnable;

    @NotNull
    @NotEmpty
    @Column(name = "BRAND")
    private String brand;

    @JsonProperty
    @Column(name = "IS_ACTIVE")
    private boolean isActive;

    @JsonProperty
    @Column(name = "IS_DELETED")
    private boolean isDeleted;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "SELLER_USER_ID")
    private Seller seller;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "CATEGORY_ID")
    private Category category;

    @JsonIgnore
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private Set<ProductVariation> productVariations;

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

    public void addProductVariation(ProductVariation productVariation) {
        if (productVariation != null) {
            if (productVariations == null) {
                productVariations = new HashSet<ProductVariation>();
            }
            productVariations.add(productVariation);
            productVariation.setProduct(this);
        }
    }

    public Boolean isActive() {
        return isActive;
    }

    public Boolean isCancellable() {
        return isCancellable;
    }
}