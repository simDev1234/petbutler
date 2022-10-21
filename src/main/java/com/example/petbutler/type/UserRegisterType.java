package com.example.petbutler.type;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum UserRegisterType {
  EMAIL("이메일"),
  GOOGLE("구글");

  private final String description;
}
