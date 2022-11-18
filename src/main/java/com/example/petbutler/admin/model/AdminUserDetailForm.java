package com.example.petbutler.admin.model;

import com.example.petbutler.model.PetDetail;
import com.example.petbutler.model.constants.UserRole;
import com.example.petbutler.model.constants.UserStatus;
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

  private UserRole userRole;

  private UserStatus userStatus;

  private int butlerLevel;

  private String phone;

  private LocalDateTime registeredAt;

  private LocalDateTime updatedAt;

  private List<PetDetail> pets;


  public List<String> getRoles() {

    if (this.userRole.isAdmin()) {
      return Arrays.asList(UserRole.ROLE_REGULAR.name(), UserRole.ROLE_ADMIN.name());

    }

    return Arrays.asList(UserRole.ROLE_ADMIN.name());
  }

}
