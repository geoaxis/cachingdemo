package com.hatimonline.spring.cachingdemo;

import com.hazelcast.config.Config;
import com.hazelcast.config.NetworkConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnCloudPlatform;
import org.springframework.boot.cloud.CloudPlatform;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnCloudPlatform(CloudPlatform.NONE)
public class LocalEmbeddedConfig {

  @Bean
  Config config() {
    Config config = new Config();
    config.setProperty("hazelcast.shutdownhook.enabled", "false");
    NetworkConfig network = config.getNetworkConfig();
    network.getJoin().getTcpIpConfig().setEnabled(false);
    network.getJoin().getMulticastConfig().setEnabled(false);
    return config;
  }
}
