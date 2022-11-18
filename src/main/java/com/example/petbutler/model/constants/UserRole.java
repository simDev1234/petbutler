package com.example.petbutler.model.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum UserRole {

  ROLE_REGULAR("일반 회원"),
  ROLE_ADMIN("관리자");

  private final String description;

  public static UserRole getUserRole(String userRole) {

    return isAdmin(userRole) ? UserRole.ROLE_ADMIN : UserRole.ROLE_REGULAR;

  }

  public static boolean isAdmin(String userRole){
    return userRole.equals(UserRole.ROLE_ADMIN.name());
  }

  public boolean isAdmin() {
    return this.equals(UserRole.ROLE_ADMIN.name());
  }
}
