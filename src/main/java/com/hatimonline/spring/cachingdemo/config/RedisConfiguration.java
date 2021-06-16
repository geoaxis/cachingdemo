package com.hatimonline.spring.cachingdemo.config;


import org.springframework.boot.autoconfigure.condition.ConditionalOnCloudPlatform;
import org.springframework.boot.cloud.CloudPlatform;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@Configuration
@EnableRedisRepositories
@ConditionalOnCloudPlatform(CloudPlatform.NONE)
public class RedisConfiguration {

  @Bean
  public LettuceConnectionFactory redisConnectionFactory(final RedisProperties redisProperties) {
    return new LettuceConnectionFactory(redisProperties.getRedisHost(), redisProperties.getRedisPort());
  }

  @Bean
  public RedisTemplate<?, ?> redisTemplate(final LettuceConnectionFactory connectionFactory) {
    RedisTemplate<byte[], byte[]> template = new RedisTemplate<>();
    template.setConnectionFactory(connectionFactory);
    return template;
  }
}
