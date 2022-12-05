package com.org.ecom.dto.product;

import com.org.ecom.dto.category.CategoryDto;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class ProductDto {

    @NotNull
    private String name;
    @NotNull
    private String brand;
    @NotNull
    private Long categoryId;
    private String description;
    private Boolean isReturnable = false;
    private Boolean isCancellable = false;
    private boolean isActive = false;
    private String categoryName;

    List<String> metadataFieldName;
    List<String> metadataFieldValues;
}
