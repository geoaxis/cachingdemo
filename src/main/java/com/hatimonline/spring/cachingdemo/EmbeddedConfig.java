package com.hatimonline.spring.cachingdemo;

import com.hazelcast.config.Config;
import com.hazelcast.config.EvictionConfig;
import com.hazelcast.config.EvictionPolicy;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.MaxSizePolicy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmbeddedConfig {

  @Value("${SERVICENAME:cachingdemo-service}")
  private String serviceName;

  @Bean
  Config config() {
    Config config = new Config();
    config.getNetworkConfig().getJoin().getTcpIpConfig().setEnabled(false);
    config.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false);
    config.getNetworkConfig().getJoin().getKubernetesConfig().setEnabled(true)
        .setProperty("namespace", "default")
        .setProperty("service-name", serviceName);

    EvictionConfig evictionConfig = new EvictionConfig()
        .setMaxSizePolicy(MaxSizePolicy.USED_HEAP_PERCENTAGE)
        .setEvictionPolicy(EvictionPolicy.LFU);

    MapConfig customerConfig = new MapConfig()
        .setTimeToLiveSeconds( 60 )
        .setEvictionConfig(evictionConfig);


    config.getMapConfigs().put("customer", customerConfig);

    MapConfig remoteCustomerConfig = new MapConfig()
        .setTimeToLiveSeconds( 10 )
        .setEvictionConfig(evictionConfig);

    config.getMapConfigs().put("remotecustomer", remoteCustomerConfig);

    return config;
  }
}
