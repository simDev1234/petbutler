package com.example.petbutler.admin.model;

import com.example.petbutler.model.PetDetailForm;
import com.example.petbutler.model.constants.UserRole;
import com.example.petbutler.persist.entity.User;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class AdminUserDetailForm {

  private String email;
  private String userRole;
  private String userStatus;
  private String phone;

  private LocalDateTime registeredAt;
  private LocalDateTime updatedAt;

  private int butlerLevel;
  private List<PetDetailForm> pets;

  public static AdminUserDetailForm fromEntity(User user) {

    return AdminUserDetailForm.builder()
        .email(user.getEmail())
        .userStatus(String.valueOf(user.getUserStatus()))
        .userRole(String.valueOf(user.getUserRole()))
        .butlerLevel(user.getButlerLevel())
        .phone(user.getPhone())
        .registeredAt(user.getRegisteredAt())
        .updatedAt(user.getUpdatedAt())
        .build();

  }

  public List<String> getRoles() {

    if (UserRole.isAdmin(UserRole.valueOf(userRole))) {
      return Arrays.asList(UserRole.ROLE_REGULAR.name(), UserRole.ROLE_ADMIN.name());
    }

    return Arrays.asList(UserRole.ROLE_REGULAR.name());
  }

}
