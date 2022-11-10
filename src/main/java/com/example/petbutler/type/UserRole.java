package com.example.petbutler.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum UserRole {

  ROLE_REGULAR("일반 회원"),
  ROLE_ADMIN("관리자");

  private final String description;

  public static UserRole findUserRole(String userRole) {

    if (UserRole.ROLE_REGULAR.isEqual(userRole)) {
      return ROLE_REGULAR;
    } else {
      return ROLE_ADMIN;
    }

  }

  public boolean isEqual(String userRole) {
    return this.name().equals(userRole);
  }
}
