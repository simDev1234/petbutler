package com.example.petbutler.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum UserRole {

  ROLE_CUSTOMER("구매자"),
  ROLE_SELLER("판매자"),
  ROLE_ADMIN("관리자");

  private final String description;

  public String asString() {
    return this.name().substring(5);
  }
}
