package com.hatimonline.spring.cachingdemo;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class Customer implements Serializable {
  public Long id;
  public String firstName;
  public String lastName;
  public LocalDateTime lastUpdated;
}
