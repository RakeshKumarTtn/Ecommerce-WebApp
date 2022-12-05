package com.org.ecom.entity.category;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Table(name = "CATEGORY_METADATA_FIELD_VALUES")
public class CategoryMetadataFieldValues implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    CategoryMetadataFieldValuesId Id = new CategoryMetadataFieldValuesId();

    @Column(name = "VALUE",unique = true)
    private String value;

    @ManyToOne
    @JoinColumn(name = "CATEGORY_ID")
    @MapsId("categoryId")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "CATEGORY_METADATA_FIELD_ID")
    @MapsId("categoryMetadataFieldId")
    private CategoryMetadataField categoryMetadataField;
}