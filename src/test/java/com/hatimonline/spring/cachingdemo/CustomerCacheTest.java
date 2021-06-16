package com.hatimonline.spring.cachingdemo;

import static com.hatimonline.spring.cachingdemo.config.CommonCacheConfig.CUSTOMER_CACHE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.hatimonline.spring.cachingdemo.config.RedisProperties;
import com.hatimonline.spring.cachingdemo.config.RemoteRedisConfiguration;
import com.hatimonline.spring.cachingdemo.config.TestRedisConfiguration;
import com.hatimonline.spring.cachingdemo.resource.dto.Customer;
import com.hatimonline.spring.cachingdemo.service.CustomerService;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Import({CustomerService.class, TestRedisConfiguration.class, RedisProperties.class, RemoteRedisConfiguration.class})
@ExtendWith(SpringExtension.class)
@EnableCaching
@ImportAutoConfiguration(classes = {
    CacheAutoConfiguration.class, RedisAutoConfiguration.class
})
@TestPropertySource(properties = {"spring.redis.host=localhost","spring.redis.port=6370"})
public class CustomerCacheTest {

  private static final long AN_ID = 1L;

  @MockBean
  private CustomerRepository customerRepository;

  @Autowired
  private CustomerService customerService;

  @Autowired
  private CacheManager cacheManager;

  @BeforeEach
  public void setup() {
    cacheManager.getCache(CUSTOMER_CACHE).invalidate();
  }

  @SneakyThrows
  @Test
  void givenRedisEmbeededCaching_whenFindCustomerByIdTwiceWithinTime_thenCustomerReturnedFromCache() {
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


  @SneakyThrows
  @Test
  void givenRedisEmbeededCaching_whenFindCustomerByIdTwiceOutsideOfTime_thenCustomerReturnedSource() {
    Customer customer = Customer.builder().id(AN_ID).lastUpdated(LocalDateTime.now()).build();
    given(customerRepository.findById(1L))
        .willReturn(Optional.of(customer));

    Optional<Customer> customerCacheHit = customerService.getCustomerById(AN_ID);
    TimeUnit.SECONDS.sleep(15);
    Optional<Customer> customerCacheMiss = customerService.getCustomerById(AN_ID);

    assertThat(customerCacheHit).isEqualTo(Optional.of(customer));
    assertThat(customerCacheMiss).isEqualTo(Optional.of(customer));

    verify(customerRepository, times(2)).findById(AN_ID);
    assertThat(customerFromCache()).isEqualTo(customer);

  }


  private Object customerFromCache() {
    return cacheManager.getCache(CUSTOMER_CACHE).get(AN_ID).get();
  }

}
