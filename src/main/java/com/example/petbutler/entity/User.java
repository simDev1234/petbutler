package com.example.petbutler.entity;

import com.example.petbutler.type.UserGrade;
import com.example.petbutler.type.UserRegisterType;
import com.example.petbutler.type.UserStatus;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity(name = "User")
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@Getter
@Setter
public class User {
  @GeneratedValue
  private Long id;

  @Id
  private String email;

  @Enumerated(value = EnumType.STRING)
  private UserRegisterType userRegisterType;

  private int butlerLevel;

  private String password;

  private String phone;

  @Enumerated(value = EnumType.STRING)
  private UserGrade userGrade;

  @Enumerated(value = EnumType.STRING)
  private UserStatus userStatus;

  @CreatedDate
  private LocalDateTime registeredAt;

  @LastModifiedDate
  private LocalDateTime updatedAt;

  @PrePersist
  public void prePersist() {
    this.userGrade = UserGrade.REGULAR_CUSTOMER;
    this.userStatus = UserStatus.NOT_AUTHORIZED;
  }
}
