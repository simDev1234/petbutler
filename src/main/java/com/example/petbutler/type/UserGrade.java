package com.example.petbutler.type;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum UserGrade {

  REGULAR_CUSTOMER("일반회원"),
  ADMIN("관리자"),
  SELLER("판매자");

  private final String description;
}
