package com.echo.backend.service.tenant;


import com.echo.backend.dto.tenant.TenantFilterDto;
import com.echo.backend.entity.tenant.TenantSubscription;
import com.echo.backend.exception.customException.ApiSystemException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TenantSubscriptionService {
    TenantSubscription create(TenantSubscription payload) throws ApiSystemException;
    TenantSubscription update(TenantSubscription payload, Long id) throws ApiSystemException;
    TenantSubscription partialUpdate(TenantSubscription payload, Long id) throws ApiSystemException;
    void delete(Long id) throws ApiSystemException;
    Page<TenantSubscription> getAll(Pageable pageable, TenantFilterDto filter) throws ApiSystemException;
    TenantSubscription getById(Long id) throws ApiSystemException;
    Page<TenantSubscription> findAllByTenantId(Long tenantId, Pageable pageable) throws ApiSystemException;
}
