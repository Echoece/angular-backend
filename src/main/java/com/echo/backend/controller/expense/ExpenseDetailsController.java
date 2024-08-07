package com.echo.backend.controller.expense;

import com.echo.backend.dto.expense.ExpenseFilter;
import com.echo.backend.entity.expense.ExpenseDetails;
import com.echo.backend.exception.customException.ApiSystemException;
import com.echo.backend.service.expense.ExpenseDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/expense_details")
@RequiredArgsConstructor
public class ExpenseDetailsController {
    private final ExpenseDetailsService expenseDetailsService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody ExpenseDetails payload) throws ApiSystemException {
        return ResponseEntity.ok(expenseDetailsService.save(payload));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody ExpenseDetails payload, @PathVariable Long id) throws ApiSystemException {
        ExpenseDetails updatedEntity = expenseDetailsService.update(payload, id);
        return new ResponseEntity<>(updatedEntity, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updatePartial(@RequestBody ExpenseDetails payload, @PathVariable Long id) throws ApiSystemException {
        ExpenseDetails updatedEntity = expenseDetailsService.updatePartial(payload, id);
        return new ResponseEntity<>(updatedEntity, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getAll(Pageable pageable, ExpenseFilter filter) {
        Page<ExpenseDetails> result = expenseDetailsService.findAll(pageable, filter);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) throws ApiSystemException {
        ExpenseDetails result = expenseDetailsService.findById(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete (@PathVariable Long id) throws ApiSystemException {
        expenseDetailsService.delete(id);
        Map<String, String> message = Map.of("message", "User Deleted Successfully");
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
