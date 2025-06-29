package com.echo.backend.controller;

import com.echo.backend.dto.PatientFilter;
import com.echo.backend.entity.Patient;
import com.echo.backend.exception.customException.ApiSystemException;
import com.echo.backend.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/patients")
@RequiredArgsConstructor
public class PatientController extends BaseController {
    private final PatientService service;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Patient payload) throws ApiSystemException {
        return buildResponseCreated(service.save(payload));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody Patient user, @PathVariable Long id) throws ApiSystemException {
        Patient updatedEntity = service.update(user, id);
        return buildResponseUpdated(updatedEntity);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updatePartial(@RequestBody Patient user, @PathVariable Long id) throws ApiSystemException {
        Patient updatedEntity = service.updatePartial(user, id);
        return buildResponseUpdated(updatedEntity);
    }

    @GetMapping
    public ResponseEntity<?> getAll(Pageable pageable, PatientFilter filter) {
        Page<Patient> result = service.findAll(pageable, filter);
        return buildPaginatedResponse(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) throws ApiSystemException {
        Patient result = service.findById(id);
        return buildResponse(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete (@PathVariable Long id) throws ApiSystemException {
        service.delete(id);
        Map<String, String> message = Map.of("message", "User Deleted Successfully");
        return buildResponseDeleted();
    }
}
