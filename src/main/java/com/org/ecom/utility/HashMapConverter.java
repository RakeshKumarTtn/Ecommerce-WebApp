package com.org.ecom.utility;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.org.ecom.entity.product.ProductVariation;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.AttributeConverter;
import java.util.Map;

public class HashMapConverter implements AttributeConverter<Map<String, Object>, String> {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ProductVariation productVariation;

    @Override
    public String convertToDatabaseColumn(Map<String, Object> variationInfo) {
        String infoJson = null;
        try {
            infoJson = objectMapper.writeValueAsString(variationInfo);
        } catch (Exception e) {

        }
        return infoJson;
    }

    @Override
    public Map<String, Object> convertToEntityAttribute(String infoJson) {

        Map<String, Object> variationInformation = null;
        try {
            variationInformation = objectMapper.readValue(infoJson, Map.class);
        } catch (Exception e) {

        }
        return variationInformation;
    }
}
