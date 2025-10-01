package com.echo.backend.auth.providers;

import com.echo.backend.auth.dto.OAuth2RequestDto;
import com.echo.backend.auth.enums.ServiceProvider;
import com.echo.backend.auth.enums.ThirdPartyServiceType;
import com.echo.backend.entity.auth.Users;

import java.util.Map;

public interface OAuth2Provider<K, T> {
    ThirdPartyServiceType getServiceType();
    ServiceProvider getServiceProvider();
    String buildAuthorizationUrl(Map<String, Object> params);
    Users registerAccount(OAuth2RequestDto payload) throws Exception;
    Boolean isAuthenticated(String uniqueId) throws Exception;
    K renewAccessToken(String refreshToken);
    T loadCredential(String uniqueId) throws Exception;
}
