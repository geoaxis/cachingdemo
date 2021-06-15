package com.hatimonline.spring.cachingdemo;

import com.hazelcast.config.EvictionConfig;
import com.hazelcast.config.MapConfig;
import java.util.Map;

public class CommonCacheConfig {
  public static final String CUSTOMER_CACHE = "customer";
  public static final String REMOTE_CUSTOMER_CACHE = "remotecustomer";

  public static Map<String, MapConfig> createAllConfigs(EvictionConfig evictionConfig) {
    return Map.of(
        CUSTOMER_CACHE, createConfig(evictionConfig, 60),
        REMOTE_CUSTOMER_CACHE, createConfig(evictionConfig, 10)
    );
  }
  private static  MapConfig createConfig(EvictionConfig evictionConfig, int i) {
    return new MapConfig()
        .setTimeToLiveSeconds(i)
        .setEvictionConfig(evictionConfig);
  }
}
