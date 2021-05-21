package com.hatimonline.spring.cachingdemo;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerController {

  @Value("${HOSTNAME:localhost}")
  private String hostname;

  private final CustomerService customerService;

  CustomerController(CustomerService customerService) {
    this.customerService = customerService;
  }

  @GetMapping("/customer/{id}")
  public ResponseEntity<Customer> getCustomerById(@PathVariable("id") Long id)
      throws InterruptedException {
    HttpHeaders responseHeaders = new HttpHeaders();
    responseHeaders.set("x-pod-hostname", hostname);

    Optional<Customer> customerOptional = customerService.getCustomerById(id);

    return customerOptional.map(customer -> ResponseEntity
        .ok().headers(responseHeaders).body(customer))
        .orElseGet(() -> ResponseEntity.notFound().headers(responseHeaders).build());
  }
}
