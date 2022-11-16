package com.scaler.springboot.cachingsample;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MathService {

    @Cacheable("factorial")
    public int factorialOf(int n) {
        log.info("Calculating factorial of number {}", n);
        if (n > 10) {
            introduceSlowness();
        }
        if (n == 0)
            return 1;
        else
            return (n * factorialOf(n - 1));
    }

    private void introduceSlowness() {
        try {
            long time = 3000L;
            Thread.sleep(time);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }
}

