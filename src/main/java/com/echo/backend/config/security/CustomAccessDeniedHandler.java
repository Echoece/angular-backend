package com.echo.backend.config.security;

import com.echo.backend.response.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        // Create an ApiResponse error with the appropriate message
        ApiResponse<Void> apiResponse = ApiResponse.error(
                HttpServletResponse.SC_FORBIDDEN,
                "Forbidden: Insufficient permissions"
        );

        // Write the ApiResponse object to the response body
        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
    }
}
