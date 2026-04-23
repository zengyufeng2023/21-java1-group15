package com.guet.dto;

import com.guet.entity.Product;
import com.guet.entity.ProductType;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ProductDto extends Product {

    private List<ProductType> types = new ArrayList<>();

    private String categoryName;

    private Integer copies;

}
