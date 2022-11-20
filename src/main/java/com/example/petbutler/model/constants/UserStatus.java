package com.example.petbutler.model.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum UserStatus{
  NOT_AUTHORIZED("미인증"),
  IN_USE("활성화"),
  STOPPED("정지"),
  WITHDRAW("탈퇴");

  private final String description;

}
