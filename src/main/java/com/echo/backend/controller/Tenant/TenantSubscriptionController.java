package com.echo.backend.controller.Tenant;

import com.echo.backend.controller.BaseController;
import com.echo.backend.dto.tenant.TenantFilterDto;
import com.echo.backend.entity.tenant.Tenant;
import com.echo.backend.entity.tenant.TenantSubscription;
import com.echo.backend.exception.customException.ApiSystemException;
import com.echo.backend.service.tenant.TenantService;
import com.echo.backend.service.tenant.TenantSubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tenant-subscriptions")
@RequiredArgsConstructor
public class TenantSubscriptionController extends BaseController {
    private final TenantSubscriptionService tenantSubscriptionService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody TenantSubscription payload) throws ApiSystemException {
        return buildResponseCreated(tenantSubscriptionService.create(payload));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody TenantSubscription payload, @PathVariable Long id) throws ApiSystemException {
        return buildResponseUpdated(tenantSubscriptionService.update(payload, id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updatePartial(@RequestBody TenantSubscription payload, @PathVariable Long id) throws ApiSystemException {
        return buildResponseUpdated(tenantSubscriptionService.partialUpdate(payload, id));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) throws ApiSystemException {
        return buildResponseDeleted();
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) throws ApiSystemException {
        return buildResponse(tenantSubscriptionService.getById(id));
    }


    @GetMapping()
    public ResponseEntity<?> getAll(Pageable pageable, TenantFilterDto filter) throws ApiSystemException {
        return buildPaginatedResponse(tenantSubscriptionService.getAll(pageable, filter));
    }

}
