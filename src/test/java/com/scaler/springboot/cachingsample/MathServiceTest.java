package com.scaler.springboot.cachingsample;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {MathServiceTest.Config.class})
class MathServiceTest {

    @SpyBean
    private MathService mathService;

    @Autowired
    private CacheManager cacheManager;

    @Configuration
    @EnableCaching
    static class Config {
        @Bean
        public MathService mathService() {
            return new MathService();
        }

        @Bean
        public CacheManager cacheManager() {
            CaffeineCacheManager cacheManager = new CaffeineCacheManager("factorial");
            cacheManager.setCaffeine(caffeineCacheBuilder());
            return cacheManager;
        }

        Caffeine<Object, Object> caffeineCacheBuilder() {
            return Caffeine.newBuilder()
                    .initialCapacity(100)
                    .maximumSize(100)
                    .expireAfterAccess(10, TimeUnit.SECONDS)
                    .recordStats();
        }
    }

    @Test
    void testCacheBehaviour() {
        int result = mathService.factorialOf(11);
        int resultFromCache = mathService.factorialOf(11);
        Mockito.verify(mathService, Mockito.times(1)).factorialOf(11);
        assertEquals(resultFromCache, result);
    }

    @Test
    void testCacheExpiry() throws InterruptedException {
        int result = mathService.factorialOf(11);

        Thread.sleep(12000);

        int resultFromCache = mathService.factorialOf(11);
        Mockito.verify(mathService, Mockito.times(2)).factorialOf(11);
        assertEquals(resultFromCache, result);
    }

    @AfterEach
    void tearDown() {
        Objects.requireNonNull(cacheManager.getCache("factorial")).clear();
    }
}