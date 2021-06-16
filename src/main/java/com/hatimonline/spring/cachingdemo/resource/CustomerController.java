package com.hatimonline.spring.cachingdemo.resource;

import com.hatimonline.spring.cachingdemo.resource.dto.Customer;
import com.hatimonline.spring.cachingdemo.service.CustomerService;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
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
    log.info("Fetching customer");
    Optional<Customer> customerOptional = customerService.getCustomerById(id);

    return customerOptional.map(customer -> ResponseEntity
        .ok().headers(responseHeaders).body(customer))
        .orElseGet(() -> ResponseEntity.notFound().headers(responseHeaders).build());
  }
}
