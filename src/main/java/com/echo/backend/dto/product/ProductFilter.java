package com.echo.backend.dto.product;

import lombok.Data;

@Data
public class ProductFilter {
    private String productName;
    private String productCategoryName;
    private Long productCategoryId;
}
