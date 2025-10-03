package com.echo.backend.controller.Tenant;

import com.echo.backend.controller.BaseController;
import com.echo.backend.dto.tenant.TenantFilterDto;
import com.echo.backend.entity.tenant.DefaultPackage;
import com.echo.backend.exception.customException.ApiSystemException;
import com.echo.backend.service.tenant.DefaultPackageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/default-package")
@RequiredArgsConstructor
public class DefaultPackageController extends BaseController {
    private final DefaultPackageService defaultPackageService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody DefaultPackage payload) throws ApiSystemException {
        return buildResponseCreated(defaultPackageService.create(payload));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody DefaultPackage payload, @PathVariable Long id) throws ApiSystemException {
        return buildResponseUpdated(defaultPackageService.update(payload, id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updatePartial(@RequestBody DefaultPackage payload, @PathVariable Long id) throws ApiSystemException {
        return buildResponseUpdated(defaultPackageService.partialUpdate(payload, id));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) throws ApiSystemException {
        return buildResponseDeleted();
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) throws ApiSystemException {
        return buildResponse(defaultPackageService.getById(id));
    }


    @GetMapping()
    public ResponseEntity<?> getAll(Pageable pageable, TenantFilterDto filter) throws ApiSystemException {
        return buildPaginatedResponse(defaultPackageService.getAll(pageable, filter));
    }

}
