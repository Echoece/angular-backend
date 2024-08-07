package com.echo.backend.controller.product;

import com.echo.backend.dto.product.ProductFilter;
import com.echo.backend.entity.Product;
import com.echo.backend.entity.ProductCategory;
import com.echo.backend.exception.customException.ApiSystemException;
import com.echo.backend.service.product.ProductCategoryService;
import com.echo.backend.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/product_category")
@RequiredArgsConstructor
public class ProductCategoryController {
    private final ProductCategoryService productCategoryService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody ProductCategory payload) throws ApiSystemException {
        return ResponseEntity.ok(productCategoryService.save(payload));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody ProductCategory user, @PathVariable Long id) throws ApiSystemException {
        ProductCategory updatedUser = productCategoryService.update(user, id);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updatePartial(@RequestBody ProductCategory user, @PathVariable Long id) throws ApiSystemException {
        ProductCategory updatedUser = productCategoryService.updatePartial(user, id);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getAll(Pageable pageable, ProductFilter filter) {
        Page<ProductCategory> result = productCategoryService.findAll(pageable, filter);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) throws ApiSystemException {
        ProductCategory result = productCategoryService.findById(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete (@PathVariable Long id) throws ApiSystemException {
        productCategoryService.delete(id);
        Map<String, String> message = Map.of("message", "User Deleted Successfully");
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
