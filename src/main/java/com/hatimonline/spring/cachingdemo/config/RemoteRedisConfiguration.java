package com.hatimonline.spring.cachingdemo.config;

import static com.hatimonline.spring.cachingdemo.config.CommonCacheConfig.CUSTOMER_CACHE;
import static com.hatimonline.spring.cachingdemo.config.CommonCacheConfig.REMOTE_CUSTOMER_CACHE;

import java.time.Duration;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;

public class RemoteRedisConfiguration {

  @Bean
  public RedisCacheConfiguration cacheConfiguration() {
    return RedisCacheConfiguration.defaultCacheConfig()
        .entryTtl(Duration.ofSeconds(60))
        .disableCachingNullValues()
        .serializeValuesWith(SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
  }

  @Bean
  public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
    return (builder) -> builder
        .withCacheConfiguration(CUSTOMER_CACHE,
            RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofSeconds(10)))
        .withCacheConfiguration(REMOTE_CUSTOMER_CACHE,
            RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofSeconds(3)));
  }

}
