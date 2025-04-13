package com.echo.backend.controller;

import com.echo.backend.entity.Users;
import com.echo.backend.exception.customException.ApiAuthorizationException;
import com.echo.backend.exception.customException.ApiNotFoundException;
import com.echo.backend.response.ApiResponse;
import com.echo.backend.service.auth.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Users payload, HttpServletRequest request) throws ApiAuthorizationException {
        return ResponseEntity.ok(userService.login(payload));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody  Users payload){
        return ResponseEntity.ok(userService.save(payload));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody Users user, @PathVariable Long id) throws ApiNotFoundException {
        Users updatedUser = userService.updatePartial(user, id);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getAll(Pageable pageable) {
        Page<Users> result = userService.findAllUsers(pageable);
        return new ResponseEntity<>(ApiResponse.paginatedResponse(result), HttpStatus.OK);

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) throws ApiNotFoundException {
        Users result = userService.findById(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete (@PathVariable Long id) throws ApiNotFoundException {
        userService.delete(id);
        Map<String, String> message = Map.of("message", "User Deleted Successfully");
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
