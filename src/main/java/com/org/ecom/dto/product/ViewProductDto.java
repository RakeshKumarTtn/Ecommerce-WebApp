package com.org.ecom.dto.product;

import com.org.ecom.entity.product.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ViewProductDto extends Product {

    Long id;
    @Column(nullable = false, unique = true)
    private String productName;

    @Column(nullable = false, unique = true)
    private String brand;

    private Boolean isCancellable;

    private Boolean isReturnable;

    private String description;

    private Boolean isActive;

    String name;
    List<String> fieldName;
    List<String> values;

    private String createdBy;

    private LocalDateTime createdDate;

    private String lastModifiedBy;

    private LocalDateTime lastModifiedDate;
}