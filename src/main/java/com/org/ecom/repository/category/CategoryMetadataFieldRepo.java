package com.org.ecom.repository.category;

import com.org.ecom.entity.category.CategoryMetadataField;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CategoryMetadataFieldRepo extends PagingAndSortingRepository<CategoryMetadataField, Long> {

    @Query(value = "SELECT * FROM CATEGORY_METADATA_FIELD cmf WHERE cmf.NAME = :fieldName", nativeQuery = true)
    Optional<CategoryMetadataField> findByCategoryMetadataFieldName(@Param("fieldName") String fieldName);
/*
    @Modifying
    @Transactional
    @Query(value = "delete from category_metadata_field_values where category_metadata_id=:category_metadata_id",nativeQuery = true)
    public void remove(@Param("category_metadata_id") Long category_metadata_id);

    @Modifying
    @Transactional
    @Query(value = "delete from category_metadata_field where id=:id",nativeQuery = true)
    public void deleteMetadatField(@Param("id") Long id);*/

    @Query(value = "select NAME from CATEGORY_METADATA_FIELD where ID = :id", nativeQuery = true)
    public String getNameOfMetadata(@Param("id") Long id);
}
