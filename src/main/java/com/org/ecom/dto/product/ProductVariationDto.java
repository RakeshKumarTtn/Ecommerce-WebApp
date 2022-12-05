package com.org.ecom.dto.product;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductVariationDto extends ProductDto {
    List<String> fields;
    List<String> values;
    Double price;
    Boolean currentActiveStatus;
    Integer QuantityAvailable;
}