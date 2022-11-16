package com.scaler.springboot.cachingsample;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {MathServiceTest.Config.class})
class MathServiceTest {

    @SpyBean
    private MathService mathService;

    @Configuration
    @EnableCaching
    static class Config {
        @Bean
        public MathService mathService() {
            return new MathService();
        }

        @Bean
        CacheManager cacheManager() {
            return new ConcurrentMapCacheManager();
        }
    }

    @Test
    public void testCacheBehaviour() {
        int result = mathService.factorialOf(11);
        int resultFromCache = mathService.factorialOf(11);
        Mockito.verify(mathService, Mockito.times(1)).factorialOf(11);
        assertEquals(resultFromCache,result);
    }

}