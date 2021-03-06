package com.hatimonline.spring.cachingdemo;

import co.elastic.apm.attach.ElasticApmAttacher;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class CachingdemoApplication {

  public static void main(String[] args) {
    if(System.getenv("KUBERNETES_SERVICE_HOST") != null) {
      ElasticApmAttacher.attach();
    }
    SpringApplication.run(CachingdemoApplication.class, args);
  }

}
