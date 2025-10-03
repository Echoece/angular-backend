package com.echo.backend.controller.Tenant;

import com.echo.backend.controller.BaseController;
import com.echo.backend.dto.tenant.TenantFilterDto;
import com.echo.backend.entity.tenant.Tenant;
import com.echo.backend.exception.customException.ApiSystemException;
import com.echo.backend.service.tenant.TenantService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tenants")
@RequiredArgsConstructor
public class TenantController extends BaseController {
    private final TenantService tenantService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody  Tenant payload) throws ApiSystemException {
        return buildResponseCreated(tenantService.create(payload));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody Tenant payload, @PathVariable Long id) throws ApiSystemException {
        return buildResponseUpdated(tenantService.update(payload, id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updatePartial(@RequestBody Tenant payload, @PathVariable Long id) throws ApiSystemException {
        return buildResponseUpdated(tenantService.partialUpdate(payload, id));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) throws ApiSystemException {
        return buildResponseDeleted();
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) throws ApiSystemException {
        return buildResponse(tenantService.getById(id));
    }


    @GetMapping()
    public ResponseEntity<?> getAll(Pageable pageable, TenantFilterDto filter) throws ApiSystemException {
        return buildPaginatedResponse(tenantService.getAll(pageable, filter));
    }

}
