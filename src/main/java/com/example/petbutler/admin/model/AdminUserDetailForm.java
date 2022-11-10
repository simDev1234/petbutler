package com.example.petbutler.admin.model;

import com.example.petbutler.model.PetDetail;
import com.example.petbutler.persist.entity.User;
import com.example.petbutler.type.UserRole;
import com.example.petbutler.type.UserStatus;
import java.time.LocalDateTime;
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

  private UserRole userRole;

  private UserStatus userStatus;

  private int butlerLevel;

  private String phone;

  private LocalDateTime registeredAt;

  private LocalDateTime updatedAt;

  private List<PetDetail> pets;

  public static AdminUserDetailForm fromUserInfo(User user) {
    return AdminUserDetailForm.builder()
        .email(user.getEmail())
        .userStatus(user.getUserStatus())
        .userRole(user.getUserRole())
        .butlerLevel(user.getButlerLevel())
        .phone(user.getPhone())
        .registeredAt(user.getRegisteredAt())
        .updatedAt(user.getUpdatedAt())
        .build();
  }
}
