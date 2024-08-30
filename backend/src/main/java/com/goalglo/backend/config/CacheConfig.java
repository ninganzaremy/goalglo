package com.goalglo.backend.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class CacheConfig {

   @Bean
   public LoadingCache<String, Integer> requestCountsPerIpAddress() {
      return Caffeine.newBuilder()
         .expireAfterWrite(1, TimeUnit.MINUTES)
         .build(key -> 0);
   }
}