package com.backsuend.coucommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class CouCommerceApplication {

  public static void main(String[] args) {
    SpringApplication.run(CouCommerceApplication.class, args);
  }
}
