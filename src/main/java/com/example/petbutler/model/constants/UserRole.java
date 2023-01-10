package com.example.petbutler.model.constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum UserRole {

  ROLE_REGULAR("일반 회원"),
  ROLE_ADMIN("관리자");

  private final String description;

  public List<UserRole> getRoles(){
    return isAdmin(this) ?  new ArrayList(Arrays.asList(ROLE_REGULAR, ROLE_ADMIN)) : new ArrayList(Arrays.asList(ROLE_REGULAR));
  }

  public static boolean isAdmin(UserRole userRole){
    return UserRole.ROLE_ADMIN.equals(userRole);
  }

  public boolean isAdmin() {
    return this.equals(UserRole.ROLE_ADMIN.name());
  }
}
