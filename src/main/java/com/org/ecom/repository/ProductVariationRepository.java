package com.org.ecom.repository;

import com.org.ecom.entity.product.ProductVariation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductVariationRepository extends JpaRepository<ProductVariation,Long> {
}
