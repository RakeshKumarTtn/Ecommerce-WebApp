package com.org.ecom.repository;

import com.org.ecom.entity.product.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Transactional
    @Modifying
    @Query(value = "update PRODUCT set IS_ACTIVE = true,IS_DELETED = false where ID =:id", nativeQuery = true)
    public void activateProduct(@Param(value = "id") Long id);

    @Transactional
    @Modifying
    @Query(value = "update PRODUCT set IS_ACTIVE = false where ID =:id", nativeQuery = true)
    public void deActivateProduct(@Param(value = "id") Long id);

    @Transactional
    @Modifying
    @Query(value = "update PRODUCT set IS_ACTIVE = false,IS_DELETED = true where ID =:id", nativeQuery = true)
    public void deleteProduct(@Param(value = "id") Long id);

    @Query(value = "select CATEGORY_ID from PRODUCT where ID=:id", nativeQuery = true)
    public Long getCategoryId(@Param("id") Long productId);

    @Query(value = "select ID from PRODUCT", nativeQuery = true)
    List<Long> getAllId(Pageable paging);

    @Query(value = "select ID from PRODUCT where CATEGORY_ID=:category_id and BRAND=:brand", nativeQuery = true)
    List<Long> getIdOfSimilarProduct(@Param("category_id") Long id, @Param("brand") String brand, Pageable paging);

    @Query(value = "select ID from PRODUCT where SELLER_USER_ID=:seller_user_id", nativeQuery = true)
    List<Long> getProductIdOfSeller(@Param("seller_user_id") Long seller_user_id, Pageable pageable);
}
