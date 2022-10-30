package com.example.petbutler.type;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum UserStatus{
  NOT_AUTHORIZED("미인증"),
  IN_USE("활성화"),
  STOPPED("정지"),
  WITHDRAW("탈퇴");

  private final String description;

}
