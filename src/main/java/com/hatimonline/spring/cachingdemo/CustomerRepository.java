package com.hatimonline.spring.cachingdemo;

import com.hatimonline.spring.cachingdemo.resource.dto.Customer;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CustomerRepository {

  final List<Customer> customerList =
      LongStream.rangeClosed(1, 100).boxed()
          .map(x -> Customer
              .builder().firstName("First" + x).lastName("Last" + x).id(x).build())
          .collect(Collectors.toList());

  public Optional<Customer> findById(long id) {
    log.info("Fetching customer {} from repository.", id);
    return customerList.stream()
        .filter(c -> c.id.compareTo(id) == 0)
        .map(c -> c.toBuilder().lastUpdated(LocalDateTime.now()).build())
        .findAny();
  }

}
