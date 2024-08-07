package com.echo.backend.service.product;

import com.echo.backend.dto.product.ProductFilter;
import com.echo.backend.entity.ProductCategory;
import com.echo.backend.exception.customException.ApiBadRequestException;
import com.echo.backend.exception.customException.ApiSystemException;
import com.echo.backend.repository.ProductCategoryRepository;
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
public class ProductCategoryServiceImpl implements ProductCategoryService{
    private final ProductCategoryRepository productCategoryRepository;

    @Override
    public ProductCategory save(ProductCategory payload) throws ApiSystemException {
        if (Objects.nonNull(payload.getId()))
            throw new ApiSystemException("cant have id");

        return productCategoryRepository.save(payload);
    }

    @Override
    public ProductCategory update(ProductCategory payload, Long id) throws ApiSystemException {
        try{
            payload.setId(id);
            return productCategoryRepository.save(payload);
        } catch (Exception e) {
            throw new ApiSystemException("system error");
        }
    }

    @Override
    public ProductCategory updatePartial(ProductCategory payload, Long id) throws ApiSystemException {
        try{
            ProductCategory savedEntity = productCategoryRepository.findById(id)
                            .orElseThrow(()-> new ApiBadRequestException("wrong id"));
            copyNonNullProperties(payload, savedEntity);
            return productCategoryRepository.save(savedEntity);
        } catch (Exception e) {
            throw new ApiSystemException("system error");
        }
    }

    @Override
    public void delete(Long id) throws ApiSystemException {
        ProductCategory savedEntity = findById(id);
        productCategoryRepository.delete(savedEntity);
    }

    @Override
    public Page<ProductCategory> findAll(Pageable pageable, ProductFilter filter) {
        return productCategoryRepository.findAll(pageable);
    }

    @Override
    public ProductCategory findById(Long id) throws ApiSystemException {
        return productCategoryRepository.findById(id)
                .orElseThrow(()-> new ApiSystemException("wrong id"));
    }
}
