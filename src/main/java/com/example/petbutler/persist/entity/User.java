package com.example.petbutler.persist.entity;

import com.example.petbutler.model.UserSignUpForm;
import com.example.petbutler.type.UserRole;
import com.example.petbutler.type.UserStatus;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.AuditOverride;
import org.springframework.security.crypto.bcrypt.BCrypt;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@AuditOverride(forClass = BaseEntity.class)
public class User extends BaseEntity{

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String email;

  @Enumerated(value = EnumType.STRING)
  private UserRole userRole;

  private String password;

  private int butlerLevel;

  private String phone;

  private boolean emailAuthYn;
  private String emailAuthKey;
  private LocalDateTime emailAuthExpiredAt;

  @Enumerated(value = EnumType.STRING)
  private UserStatus userStatus;

  public static User of(UserSignUpForm form) {
    return User.builder()
        .email(form.getEmail())
        .password(BCrypt.hashpw(form.getPassword(), BCrypt.gensalt()))
        .butlerLevel(form.getButlerLevel())
        .userRole(UserRole.ROLE_REGULAR)
        .userStatus(UserStatus.NOT_AUTHORIZED)
        .emailAuthYn(false)
        .emailAuthKey(UUID.randomUUID().toString().replace("-", ""))
        .emailAuthExpiredAt(LocalDateTime.now().plusDays(1))
        .build();
  }

  public void authorize() {
    this.setUserStatus(UserStatus.IN_USE);
    this.setEmailAuthYn(true);
    this.setEmailAuthKey("");
    this.setEmailAuthExpiredAt(null);
  }

}
