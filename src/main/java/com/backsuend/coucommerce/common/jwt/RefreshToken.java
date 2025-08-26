package com.backsuend.coucommerce.common.jwt;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class RefreshToken {

  @Id
  @Column(name = "rt_key") // user email
  private String key;

  @Column(name = "rt_value") // refresh token string
  private String value;

  @Builder
  public RefreshToken(String key, String value) {
    this.key = key;
    this.value = value;
  }

  public RefreshToken updateValue(String token) {
    this.value = token;
    return this;
  }
}
