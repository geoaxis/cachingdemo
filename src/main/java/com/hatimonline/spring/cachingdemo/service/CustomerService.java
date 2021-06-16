package com.hatimonline.spring.cachingdemo.service;

import static com.hatimonline.spring.cachingdemo.config.CommonCacheConfig.CUSTOMER_CACHE;

import com.hatimonline.spring.cachingdemo.CustomerRepository;
import com.hatimonline.spring.cachingdemo.resource.dto.Customer;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@CacheConfig(cacheNames = CUSTOMER_CACHE)
@Slf4j
@RequiredArgsConstructor
public class CustomerService {

  private final CustomerRepository customerRepository;

  final List<Customer> customerList =
      LongStream.rangeClosed(1,100).boxed()
          .map( x -> Customer
              .builder().firstName("First" + x).lastName("Last" + x).id(x).build())
          .collect(Collectors.toList());


  @Cacheable(key = "#id")
  public Optional<Customer> getCustomerById(long id)
     throws InterruptedException {
    log.info("No cache found for  customer {}. Doing expensive 3 sec operation and then filtering results.", id);
    TimeUnit.SECONDS.sleep(3);
      return customerRepository.findById(id);
  }
}
