package com.echo.backend.service.product;


import com.echo.backend.dto.product.ProductFilter;
import com.echo.backend.entity.Product;
import com.echo.backend.exception.customException.ApiSystemException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    Product save(Product payload) throws ApiSystemException;
    Product update(Product payload, Long id) throws ApiSystemException;
    Product updatePartial(Product payload, Long id) throws ApiSystemException;
    void delete(Long id) throws ApiSystemException;
    Page<Product> findAll(Pageable pageable, ProductFilter filter);
    Product findById(Long id) throws ApiSystemException;

}
