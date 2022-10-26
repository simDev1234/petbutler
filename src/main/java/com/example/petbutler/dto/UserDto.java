package com.example.petbutler.dto;

import com.example.petbutler.domain.user.User;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@Getter
@Setter
public class UserDto {
  private String email;

  private String phone;

  private int butlerLevel;

  public static UserDto fromEntity(User user) {
    return UserDto.builder()
        .email(user.getEmail())
        .phone(user.getPhone())
        .build();
  }
}
