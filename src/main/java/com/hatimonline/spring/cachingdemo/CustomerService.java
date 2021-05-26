package com.hatimonline.spring.cachingdemo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@CacheConfig(cacheNames = "customer")
@Slf4j
public class CustomerService {

  final List<Customer> customerList =
      LongStream.rangeClosed(1,100).boxed()
          .map( x -> Customer
              .builder().firstName("First" + x).lastName("Last" + x).id(x).build())
          .collect(Collectors.toList());


  @Cacheable(key = "#id")
  public Optional<Customer> getCustomerById(long id)
     throws InterruptedException {
    log.info("No cache found for  customer. Doing expensive 3 sec operation and then filtering customer");
    var cutomer = customerList.stream()
          .filter(c -> c.id.compareTo(id) == 0)
          .map(c -> c.toBuilder().lastUpdated(LocalDateTime.now()).build())
          .findAny();
    TimeUnit.SECONDS.sleep(3);
      return cutomer;
  }
}
