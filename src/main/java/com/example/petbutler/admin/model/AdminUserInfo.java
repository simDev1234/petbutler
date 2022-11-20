package com.example.petbutler.admin.model;

import com.example.petbutler.model.constants.UserRole;
import com.example.petbutler.model.constants.UserStatus;
import com.example.petbutler.persist.entity.User;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AdminUserInfo {

  private String email;
  private String phone;
  private UserRole userRole;
  private UserStatus userStatus;
  private int butlerLevel;
  private LocalDateTime registeredAt;
  private LocalDateTime updatedAt;

  public static AdminUserInfo fromEntity(User user) {
    return AdminUserInfo.builder()
        .email(user.getEmail())
        .phone(user.getPhone())
        .userRole(UserRole.getUserRole(user.getUserRoles()))
        .userStatus(user.getUserStatus())
        .butlerLevel(user.getButlerLevel())
        .registeredAt(user.getRegisteredAt())
        .updatedAt(user.getUpdatedAt())
        .build();
  }


}
