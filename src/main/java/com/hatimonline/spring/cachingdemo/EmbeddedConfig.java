package com.hatimonline.spring.cachingdemo;

import com.hazelcast.config.Config;
import com.hazelcast.config.EvictionConfig;
import com.hazelcast.config.EvictionPolicy;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.MaxSizePolicy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmbeddedConfig {

  @Bean
  Config config() {
    Config config = new Config();
    config.getNetworkConfig().getJoin().getTcpIpConfig().setEnabled(false);
    config.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false);
    config.getNetworkConfig().getJoin().getKubernetesConfig().setEnabled(true)
        .setProperty("namespace", "default")
        .setProperty("service-name", "cachingdemo-service");

    EvictionConfig evictionConfig = new EvictionConfig()
        .setMaxSizePolicy(MaxSizePolicy.USED_HEAP_PERCENTAGE)
        .setEvictionPolicy(EvictionPolicy.LFU);
    MapConfig mapConfig = new MapConfig()
        .setTimeToLiveSeconds( 20 )
        .setEvictionConfig(evictionConfig);
    config.getMapConfigs().put("customer", mapConfig);

    return config;
  }
}
