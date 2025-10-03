package com.echo.backend.service.tenant;


import com.echo.backend.dto.tenant.TenantFilterDto;
import com.echo.backend.entity.tenant.Feature;
import com.echo.backend.exception.customException.ApiSystemException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FeatureService {
    Feature create(Feature payload) throws ApiSystemException;
    Feature update(Feature payload, Long id) throws ApiSystemException;
    Feature partialUpdate(Feature payload, Long id) throws ApiSystemException;
    void delete(Long id) throws ApiSystemException;
    Page<Feature> getAll(Pageable pageable, TenantFilterDto filter) throws ApiSystemException;
    Feature getById(Long id) throws ApiSystemException;
}
