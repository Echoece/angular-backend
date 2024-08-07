package com.echo.backend.service.product;

import com.echo.backend.dto.product.ProductFilter;
import com.echo.backend.entity.Product;
import com.echo.backend.exception.customException.ApiBadRequestException;
import com.echo.backend.exception.customException.ApiSystemException;
import com.echo.backend.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static com.echo.backend.utility.Utility.copyNonNullProperties;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService{
    private final ProductRepository productRepository;

    @Override
    public Product save(Product payload) throws ApiSystemException {
        if (Objects.nonNull(payload.getId()))
            throw new ApiSystemException("cant have id");

        return productRepository.save(payload);
    }

    @Override
    public Product update(Product payload, Long id) throws ApiSystemException {
        try{
            payload.setId(id);
            return productRepository.save(payload);
        } catch (Exception e) {
            throw new ApiSystemException("system error");
        }
    }

    @Override
    public Product updatePartial(Product payload, Long id) throws ApiSystemException {
        try{
            Product savedEntity = productRepository.findById(id)
                            .orElseThrow(()-> new ApiBadRequestException("wrong id"));
            copyNonNullProperties(payload, savedEntity);
            return productRepository.save(savedEntity);
        } catch (Exception e) {
            throw new ApiSystemException("system error");
        }
    }

    @Override
    public void delete(Long id) throws ApiSystemException {
        Product savedEntity = findById(id);
        productRepository.delete(savedEntity);
    }

    @Override
    public Page<Product> findAll(Pageable pageable, ProductFilter filter) {
        return productRepository.findAll(pageable);
    }

    @Override
    public Product findById(Long id) throws ApiSystemException {
        return productRepository.findById(id)
                .orElseThrow(()-> new ApiSystemException("wrong id"));
    }
}
