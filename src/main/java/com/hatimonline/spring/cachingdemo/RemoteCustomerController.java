package com.hatimonline.spring.cachingdemo;

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
public class RemoteCustomerController {

  private RemoteCustomerService remoteCustomerService;

  @Value("${HOSTNAME:localhost}")
  private String hostname;

  RemoteCustomerController(RemoteCustomerService remoteCustomerService) {
    this.remoteCustomerService = remoteCustomerService;
  }

  @GetMapping("/remotecustomer/{id}")
  public ResponseEntity<Customer> callExternalServiceByName(@PathVariable("id") Long id)
      throws InterruptedException {
    log.info("Fetching remote customer (from Cache or real)");
    HttpHeaders responseHeaders = new HttpHeaders();
    responseHeaders.set("x-pod-hostname", hostname);

    Optional<Customer> customerOptional = remoteCustomerService.getRemoteCustomer(id);

    return customerOptional.map(customer -> ResponseEntity
        .ok().headers(responseHeaders).body(customer))
        .orElseGet(() -> ResponseEntity.notFound().headers(responseHeaders).build());

  }
}
