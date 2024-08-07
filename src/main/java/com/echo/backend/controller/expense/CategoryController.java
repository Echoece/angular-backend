package com.echo.backend.controller.expense;

import com.echo.backend.dto.expense.ExpenseFilter;
import com.echo.backend.entity.expense.Category;
import com.echo.backend.exception.customException.ApiSystemException;
import com.echo.backend.service.expense.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Category payload) throws ApiSystemException {
        return ResponseEntity.ok(categoryService.save(payload));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody Category payload, @PathVariable Long id) throws ApiSystemException {
        Category updatedEntity = categoryService.update(payload, id);
        return new ResponseEntity<>(updatedEntity, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updatePartial(@RequestBody Category payload, @PathVariable Long id) throws ApiSystemException {
        Category updatedEntity = categoryService.updatePartial(payload, id);
        return new ResponseEntity<>(updatedEntity, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getAll(Pageable pageable, ExpenseFilter filter) {
        Page<Category> result = categoryService.findAll(pageable, filter);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) throws ApiSystemException {
        Category result = categoryService.findById(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete (@PathVariable Long id) throws ApiSystemException {
        categoryService.delete(id);
        Map<String, String> message = Map.of("message", "User Deleted Successfully");
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
