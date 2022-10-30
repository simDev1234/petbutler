package com.example.petbutler.type;

import org.springframework.core.convert.converter.Converter;

public class UserRoleConverter implements Converter<String, UserRole> {
  @Override
  public UserRole convert(String role) {

    if (role.equalsIgnoreCase("admin")){
      return UserRole.ROLE_ADMIN;
    } else if (role.equalsIgnoreCase("seller")) {
      return UserRole.ROLE_SELLER;
    } else {
      return UserRole.ROLE_CUSTOMER;
    }

  }
}
