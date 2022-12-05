package com.org.ecom.repository.category;

import com.org.ecom.entity.category.CategoryMetadataFieldValues;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CategoryMetadataFieldValuesRepo extends PagingAndSortingRepository<CategoryMetadataFieldValues, Long> {

    Optional<CategoryMetadataFieldValues> findByCategoryMetadataFieldIdAndCategoryId(Long categoryMetadataFieldId, Long categoryFieldId);

    Optional<CategoryMetadataFieldValues> findByCategoryId(Long categoryId);

    @Query(value = "select CATEGORY_METADATA_FIELD_ID from CATEGORY_METADATA_FIELD_VALUES where CATEGORY_ID=:category_id", nativeQuery = true)
    List<Long> getMetadataId(@Param(value = "category_id") Long id);

    @Query(value = "select VALUE from CATEGORY_METADATA_FIELD_VALUES where " +
            "CATEGORY_ID=:category_id and CATEGORY_METADATA_FIELD_ID=:category_metadata_field_id", nativeQuery = true)
    String getFieldValuesForCompositeKey(@Param(value = "category_id") Long category_id,
                                         @Param(value = "category_metadata_field_id") Long category_metadata_id);

    /*
    @Query(value = "select * from category_metadata_field_values where " +
            "category_id=:category_id and category_metadata_field_id=:category_metadata_field_id ", nativeQuery = true)
    public CategoryMetadataFieldValues getFieldValues(@Param(value = "category_id") Long category_id,
                                                      @Param(value = "category_metadata_field_id") Long category_metadata_id);*/
}