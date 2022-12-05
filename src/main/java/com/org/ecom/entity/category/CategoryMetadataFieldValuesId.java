package com.org.ecom.entity.category;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryMetadataFieldValuesId implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long categoryId;

    private Long categoryMetadataFieldId;
}
