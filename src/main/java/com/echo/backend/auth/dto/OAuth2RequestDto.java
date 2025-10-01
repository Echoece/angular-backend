package com.echo.backend.auth.dto;

import com.echo.backend.auth.enums.ServiceProvider;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
public class OAuth2RequestDto {
    private String code;
    private ServiceProvider provider;
    private Map<String, Object> payload;
}
