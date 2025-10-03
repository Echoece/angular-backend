package com.echo.backend.service.tenant;


import com.echo.backend.dto.tenant.TenantFilterDto;
import com.echo.backend.entity.tenant.DefaultPackage;
import com.echo.backend.exception.customException.ApiSystemException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DefaultPackageService {
    DefaultPackage create(DefaultPackage payload) throws ApiSystemException;
    DefaultPackage update(DefaultPackage payload, Long id) throws ApiSystemException;
    DefaultPackage partialUpdate(DefaultPackage payload, Long id) throws ApiSystemException;
    void delete(Long id) throws ApiSystemException;
    Page<DefaultPackage> getAll(Pageable pageable, TenantFilterDto filter) throws ApiSystemException;
    DefaultPackage getById(Long id) throws ApiSystemException;
}
