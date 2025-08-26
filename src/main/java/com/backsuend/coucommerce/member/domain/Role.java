package com.backsuend.coucommerce.member.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
  BUYER("ROLE_BUYER", "구매자"),
  SELLER("ROLE_SELLER", "판매자"),
  ADMIN("ROLE_ADMIN", "관리자");

  private final String key;
  private final String title;
}
