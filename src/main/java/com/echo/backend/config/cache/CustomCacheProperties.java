package com.echo.backend.config.cache;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Getter @Setter
@ConfigurationProperties(prefix = "application.cache")
public class CustomCacheProperties {
    private Map<String, CacheSpec> specs = new HashMap<>();

    @Getter
    @Setter
    public static class CacheSpec {
        private int maxSize;
        private int ttlSeconds;
    }
}
