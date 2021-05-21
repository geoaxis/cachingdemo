package com.hatimonline.spring.cachingdemo;

import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerController {

  private final CustomerService customerService;

  CustomerController(CustomerService customerService) {
    this.customerService = customerService;
  }

  @GetMapping("/customer/{id}")
  public ResponseEntity<Customer> getCustomerById(@PathVariable("id") Long id)
      throws InterruptedException {

    Optional<Customer> customerOptional = customerService.getCustomerById(id);

    return customerOptional.map(customer -> ResponseEntity
        .ok(customer))
        .orElseGet(() -> ResponseEntity.notFound().build());
  }
}
