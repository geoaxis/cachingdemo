package com.hatimonline.spring.cachingdemo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@CacheConfig(cacheNames = "customer")
public class CustomerService {

  final List<Customer> customerList =
      LongStream.rangeClosed(1,100).boxed()
          .map( x -> Customer
              .builder().firstName("First" + x).lastName("Last" + x).id(x).build())
          .collect(Collectors.toList());


  @Cacheable(key = "#id")
  public Optional<Customer> getCustomerById(long id)
     throws InterruptedException {
      var cutomer = customerList.stream()
          .filter(c -> c.id.compareTo(id) == 0)
          .map(c -> c.toBuilder().lastUpdated(LocalDateTime.now()).build())
          .findAny();
      TimeUnit.SECONDS.sleep(5);
      return cutomer;
  }
}
