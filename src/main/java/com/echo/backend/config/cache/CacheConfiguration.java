package com.echo.backend.config.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/*
* example in application properties:
* application.cache.specs.files.maxSize=100
* application.cache.specs.files.ttlSeconds=600
*
* now we can use this annotation:
*
* // 'files' cache must match the property key in application.properties
* @Cacheable(value = "files", key = "#fileId")
    public File getFileById(String fileId) {
        System.out.println("Fetching file " + fileId + " from database...");
        return new File(fileId, "Some file content");
    }
*
* 1. first call:
* File file = fileService.getFileById("123");
* Cache miss → method executes → result stored in Caffeine cache files with TTL 600 seconds.
*
* 2. subsequent calls:
* File file = fileService.getFileById("123");
* Cache hit → method does NOT execute, returns cached result.
*
* 3.After TTL expires (600 seconds): Cache entry automatically expires → next call recomputes and caches again.
*
*
* this is basic workflow, if we want to make different cache config other than file , we just need to change the key
* */

@Configuration
@RequiredArgsConstructor
@EnableCaching
public class CacheConfiguration {
    private final CustomCacheProperties customCacheProperties;
    @Bean
    public CacheManager cacheManager() {
        Map<String, Cache> cacheMap = new HashMap<>();

        for (Map.Entry<String, CustomCacheProperties.CacheSpec> entry : customCacheProperties.getSpecs().entrySet()) {
            String cacheName = entry.getKey();
            CustomCacheProperties.CacheSpec spec = entry.getValue();

            Caffeine<Object, Object> caffeine = Caffeine.newBuilder()
                    .expireAfterAccess(Duration.ofSeconds(spec.getTtlSeconds()))
                    .maximumSize(spec.getMaxSize());

            cacheMap.put(cacheName, new CaffeineCache(cacheName, caffeine.build()));
        }

        SimpleCacheManager manager = new SimpleCacheManager();
        manager.setCaches(cacheMap.values());
        return manager;
    }
}
