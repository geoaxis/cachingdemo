package com.hatimonline.spring.cachingdemo;

import static org.assertj.core.api.Assertions.assertThat;

import com.hatimonline.spring.cachingdemo.resource.dto.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class CachingdemoApplicationTests {

  @LocalServerPort
  private int port;

  @Autowired
  private TestRestTemplate restTemplate;

  @Test
  void contextLoads() {
    assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/customer/1",
        Customer.class)).isNotNull();
  }

}
