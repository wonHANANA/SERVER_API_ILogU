package com.onehana.server_ilogu.config;

import com.google.common.util.concurrent.RateLimiter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RateLimitConfiguration {

    @Bean
    public RateLimiter rateLimiter() {
        return RateLimiter.create(12.0 / 60.0);
    }
}
