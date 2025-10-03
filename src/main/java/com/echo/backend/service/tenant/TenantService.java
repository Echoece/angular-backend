package com.echo.backend.service.tenant;


import com.echo.backend.dto.tenant.TenantFilterDto;
import com.echo.backend.entity.tenant.Tenant;
import com.echo.backend.exception.customException.ApiSystemException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TenantService {
    Tenant create(Tenant payload) throws ApiSystemException;
    Tenant update(Tenant payload, Long id) throws ApiSystemException;
    Tenant partialUpdate(Tenant payload, Long id) throws ApiSystemException;
    void delete(Long id) throws ApiSystemException;
    Page<Tenant> getAll(Pageable pageable, TenantFilterDto filter) throws ApiSystemException;
    Tenant getById(Long id) throws ApiSystemException;
}
