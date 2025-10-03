package com.echo.backend.controller.Tenant;

import com.echo.backend.controller.BaseController;
import com.echo.backend.dto.tenant.TenantFilterDto;
import com.echo.backend.entity.tenant.Feature;
import com.echo.backend.entity.tenant.Tenant;
import com.echo.backend.exception.customException.ApiSystemException;
import com.echo.backend.service.tenant.FeatureService;
import com.echo.backend.service.tenant.TenantService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/feature")
@RequiredArgsConstructor
public class FeatureController extends BaseController {
    private final FeatureService featureService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Feature payload) throws ApiSystemException {
        return buildResponseCreated(featureService.create(payload));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody Feature payload, @PathVariable Long id) throws ApiSystemException {
        return buildResponseUpdated(featureService.update(payload, id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updatePartial(@RequestBody Feature payload, @PathVariable Long id) throws ApiSystemException {
        return buildResponseUpdated(featureService.partialUpdate(payload, id));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) throws ApiSystemException {
        return buildResponseDeleted();
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) throws ApiSystemException {
        return buildResponse(featureService.getById(id));
    }


    @GetMapping()
    public ResponseEntity<?> getAll(Pageable pageable, TenantFilterDto filter) throws ApiSystemException {
        return buildPaginatedResponse(featureService.getAll(pageable, filter));
    }

}
