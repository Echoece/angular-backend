package com.echo.backend.service.product;


import com.echo.backend.dto.product.ProductFilter;
import com.echo.backend.entity.ProductCategory;
import com.echo.backend.exception.customException.ApiSystemException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductCategoryService {
    ProductCategory save(ProductCategory payload) throws ApiSystemException;
    ProductCategory update(ProductCategory payload, Long id) throws ApiSystemException;
    ProductCategory updatePartial(ProductCategory payload, Long id) throws ApiSystemException;
    void delete(Long id) throws ApiSystemException;
    ProductCategory findById(Long id) throws ApiSystemException;
    Page<ProductCategory> findAll(Pageable pageable, ProductFilter filter);

}
