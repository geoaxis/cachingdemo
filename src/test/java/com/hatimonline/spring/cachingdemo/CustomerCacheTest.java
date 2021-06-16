package com.hatimonline.spring.cachingdemo;

import static com.hatimonline.spring.cachingdemo.config.CommonCacheConfig.CUSTOMER_CACHE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.hatimonline.spring.cachingdemo.config.EmbeddedLocalCacheConfig;
import com.hatimonline.spring.cachingdemo.resource.dto.Customer;
import com.hatimonline.spring.cachingdemo.service.CustomerService;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.boot.autoconfigure.hazelcast.HazelcastAutoConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Import({EmbeddedLocalCacheConfig.class, CustomerService.class})
@ExtendWith(SpringExtension.class)
@EnableCaching
@ImportAutoConfiguration(classes = {
    CacheAutoConfiguration.class, HazelcastAutoConfiguration.class
})
public class CustomerCacheTest {

  private static final long AN_ID = 1L;

  @MockBean
  private CustomerRepository customerRepository;

  @Autowired
  private CustomerService customerService;

  @Autowired
  private CacheManager cacheManager;

  @SneakyThrows
  @Test
  void givenHazelcastCaching_whenFindCustomerById_thenCustomerReturnedFromCache() {
    Customer customer = Customer.builder().id(AN_ID).lastUpdated(LocalDateTime.now()).build();
    given(customerRepository.findById(1L))
        .willReturn(Optional.of(customer));

    Optional<Customer> customerCacheHit = customerService.getCustomerById(AN_ID);
    Optional<Customer> customerCacheMiss = customerService.getCustomerById(AN_ID);

    assertThat(customerCacheHit).isEqualTo(Optional.of(customer));
    assertThat(customerCacheMiss).isEqualTo(Optional.of(customer));

    verify(customerRepository, times(1)).findById(AN_ID);
    assertThat(customerFromCache()).isEqualTo(customer);

  }


  private Object customerFromCache() {
    return cacheManager.getCache(CUSTOMER_CACHE).get(AN_ID).get();
  }

}
