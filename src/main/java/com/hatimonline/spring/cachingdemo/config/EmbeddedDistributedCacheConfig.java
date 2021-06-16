package com.hatimonline.spring.cachingdemo.config;

import static com.hatimonline.spring.cachingdemo.config.CommonCacheConfig.createAllConfigs;

import com.hazelcast.config.Config;
import com.hazelcast.config.EvictionConfig;
import com.hazelcast.config.EvictionPolicy;
import com.hazelcast.config.MaxSizePolicy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnCloudPlatform;
import org.springframework.boot.cloud.CloudPlatform;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnCloudPlatform(CloudPlatform.KUBERNETES)
public class EmbeddedDistributedCacheConfig {

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

    config.getMapConfigs().putAll(createAllConfigs(evictionConfig));

    return config;
  }
}
