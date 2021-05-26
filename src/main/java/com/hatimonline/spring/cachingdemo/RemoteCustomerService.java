package com.hatimonline.spring.cachingdemo;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
@CacheConfig(cacheNames = "remotecustomer")
public class RemoteCustomerService {

  private RestTemplate restTemplate;

  RemoteCustomerService(RestTemplateBuilder restTemplateBuilder) {
    this.restTemplate = restTemplateBuilder.build();
  }

  @Cacheable(key = "#id")
  public Optional<Customer> getRemoteCustomer(Long id) throws InterruptedException {
    log.info("No cache found for remote customer. Doing expensvie 3 sec operation and then calling customer endpoint");
    TimeUnit.SECONDS.sleep(3);
    String internalServiceUrl = "http://cachingdemointernal-service.default.svc.cluster.local:8080/customer/";
    return Optional.ofNullable(restTemplate.getForObject(internalServiceUrl + id, Customer.class));
  }

}
