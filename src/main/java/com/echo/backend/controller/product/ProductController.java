package com.echo.backend.controller.product;

import com.echo.backend.dto.product.ProductFilter;
import com.echo.backend.entity.Product;
import com.echo.backend.exception.customException.ApiSystemException;
import com.echo.backend.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody  Product payload) throws ApiSystemException {
        return ResponseEntity.ok(productService.save(payload));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody Product user, @PathVariable Long id) throws ApiSystemException {
        Product updatedUser = productService.update(user, id);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updatePartial(@RequestBody Product user, @PathVariable Long id) throws ApiSystemException {
        Product updatedUser = productService.updatePartial(user, id);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getAll(Pageable pageable, ProductFilter filter) {
        Page<Product> result = productService.findAll(pageable, filter);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) throws ApiSystemException {
        Product result = productService.findById(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete (@PathVariable Long id) throws ApiSystemException {
        productService.delete(id);
        Map<String, String> message = Map.of("message", "User Deleted Successfully");
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
