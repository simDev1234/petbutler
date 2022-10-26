package com.example.petbutler.domain.user;

import com.example.petbutler.type.UserGrade;
import com.example.petbutler.type.UserRegisterType;
import com.example.petbutler.type.UserStatus;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode
@Builder
@Getter
@Setter
public class User {

  @Id
  private String email;

  private String password;

  private String phone;

  private int butlerLevel;

  private boolean emailAuthYn;

  private String emailAuthKey;

  @Enumerated(value = EnumType.STRING)
  private UserRegisterType userRegisterType;

  @Enumerated(value = EnumType.STRING)
  private UserGrade userGrade;

  @Enumerated(value = EnumType.STRING)
  private UserStatus userStatus;

  @CreatedDate
  private LocalDateTime registeredAt;

  @LastModifiedDate
  private LocalDateTime updatedAt;

  private LocalDateTime emailAuthAt;

}
