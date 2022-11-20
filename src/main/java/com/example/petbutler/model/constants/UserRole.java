package com.example.petbutler.model.constants;

import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum UserRole {

  ROLE_REGULAR("일반 회원"),
  ROLE_ADMIN("관리자");

  private final String description;

  public static UserRole getUserRole(String userRole) {

    return isAdmin(userRole) ? ROLE_ADMIN : ROLE_REGULAR;

  }

  public static UserRole getUserRole(List<String> userRoles){
    return isAdmin(userRoles) ? ROLE_ADMIN : ROLE_REGULAR;
  }

  public static boolean isAdmin(String userRole){
    return userRole.equals(UserRole.ROLE_ADMIN.name());
  }

  public static boolean isAdmin(List<String> userRoles) {
    return userRoles.contains(ROLE_ADMIN.name()) ? true : false;
  }

  public boolean isAdmin() {
    return this.equals(UserRole.ROLE_ADMIN.name());
  }
}
