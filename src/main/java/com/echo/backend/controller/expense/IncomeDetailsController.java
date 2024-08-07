package com.echo.backend.controller.expense;

import com.echo.backend.dto.expense.ExpenseFilter;
import com.echo.backend.entity.expense.IncomeDetails;
import com.echo.backend.exception.customException.ApiSystemException;
import com.echo.backend.service.expense.IncomeDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/income_details")
@RequiredArgsConstructor
public class IncomeDetailsController {
    private final IncomeDetailsService incomeDetailsService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody IncomeDetails payload) throws ApiSystemException {
        return ResponseEntity.ok(incomeDetailsService.save(payload));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody IncomeDetails payload, @PathVariable Long id) throws ApiSystemException {
        IncomeDetails updatedEntity = incomeDetailsService.update(payload, id);
        return new ResponseEntity<>(updatedEntity, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updatePartial(@RequestBody IncomeDetails payload, @PathVariable Long id) throws ApiSystemException {
        IncomeDetails updatedEntity = incomeDetailsService.updatePartial(payload, id);
        return new ResponseEntity<>(updatedEntity, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getAll(Pageable pageable, ExpenseFilter filter) {
        Page<IncomeDetails> result = incomeDetailsService.findAll(pageable, filter);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) throws ApiSystemException {
        IncomeDetails result = incomeDetailsService.findById(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete (@PathVariable Long id) throws ApiSystemException {
        incomeDetailsService.delete(id);
        Map<String, String> message = Map.of("message", "User Deleted Successfully");
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
