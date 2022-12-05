package com.org.ecom.repository.category;

import com.org.ecom.entity.category.Category;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends PagingAndSortingRepository<Category, Long> {

    @Query(value = "SELECT * FROM CATEGORY c WHERE c.NAME = :categoryName", nativeQuery = true)
    Optional<Category> findByCategoryName(@Param("categoryName") String categoryName);

    @Query(value = "SELECT * FROM CATEGORY c WHERE c.PARENT_ID= :id", nativeQuery = true)
    List<Category> findChildCategories(@Param("id") Long id);

    @Query(value = "SELECT * FROM CATEGORY c WHERE c.PARENT_ID IS NULL", nativeQuery = true)
    List<Category> findCategoryByNull();

    @Query(value = "SELECT c1.* FROM CATEGORY c1 " +
            "LEFT OUTER JOIN CATEGORY c2 ON c2.PARENT_ID = c1.CATEGORY_ID " +
            "WHERE c2.CATEGORY_ID IS NULL", nativeQuery = true)
    Optional<List<Category>> findLeafNodes();

    @Query(value = "select exists(select * from CATEGORY where PARENT_ID=:parent_id)", nativeQuery = true)
    int checkIfLeaf(@Param("parent_id") Long parent_id);
}
