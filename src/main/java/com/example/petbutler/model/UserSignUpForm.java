package com.example.petbutler.model;

import com.example.petbutler.model.constants.UserRole;
import com.example.petbutler.model.constants.UserStatus;
import com.example.petbutler.persist.entity.User;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCrypt;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class UserSignUpForm {

  @NotBlank
  private String email;

  @NotBlank
  private String password;

  private String phone;

  @NotNull
  private int butlerLevel;


  public User toEntity() {

    return User.builder()
        .email(this.getEmail())
        .password(this.getPassword())
        .butlerLevel(this.getButlerLevel())
        .phone(this.getPhone())
        .userRoles(Arrays.asList(UserRole.ROLE_REGULAR.name()))
        .userStatus(UserStatus.NOT_AUTHORIZED)
        .emailAuthYn(false)
        .emailAuthKey(UUID.randomUUID().toString().replace("-", ""))
        .emailAuthExpiredAt(LocalDateTime.now().plusDays(1))
        .build();
  }
}
