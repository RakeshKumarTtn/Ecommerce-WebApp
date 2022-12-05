package com.org.ecom.dto.product;

import java.util.List;

public class ViewProductDtoCustomer extends ViewProductDto {
    List<String> links;

    public List<String> getLinks() {
        return links;
    }

    public void setLinks(List<String> links) {
        this.links = links;
    }
}