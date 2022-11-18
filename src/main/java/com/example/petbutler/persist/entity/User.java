package com.example.petbutler.persist.entity;

import com.example.petbutler.admin.model.AdminUserDetailForm;
import com.example.petbutler.model.constants.UserRole;
import com.example.petbutler.model.constants.UserStatus;
import com.example.petbutler.persist.converter.StringListConverter;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Convert;
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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@AuditOverride(forClass = BaseEntity.class)
public class User extends BaseEntity implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // 필수 정보
  private String email;
  private String password;
  private int butlerLevel;

  // 선택 정보
  private String phone;

  // 이메일 인증
  private boolean emailAuthYn;
  private String emailAuthKey;
  private LocalDateTime emailAuthExpiredAt;

  // 회원 역할
  @Convert(converter = StringListConverter.class)
  private List<String> userRoles;

  // 회원 상태
  @Enumerated(value = EnumType.STRING)
  private UserStatus userStatus;

  // user detail override
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return this.userRoles.stream()
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());
  }

  @Override
  public String getUsername() {
    return this.email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return this.userStatus.equals(UserStatus.STOPPED);
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return this.userStatus.equals(UserStatus.IN_USE);
  }

  // 이메일 인증 후 활성화
  public void authorize() {
    this.setUserStatus(UserStatus.IN_USE);
    this.setEmailAuthYn(true);
    this.setEmailAuthKey("");
    this.setEmailAuthExpiredAt(null);
  }

  public AdminUserDetailForm toUserDetailForm() {

    boolean isAdmin = this.getUserRoles().contains(UserRole.ROLE_ADMIN);

    UserRole role = isAdmin? UserRole.ROLE_ADMIN : UserRole.ROLE_REGULAR;

    return AdminUserDetailForm.builder()
        .email(this.getEmail())
        .userStatus(this.getUserStatus())
        .userRole(role)
        .butlerLevel(this.getButlerLevel())
        .phone(this.getPhone())
        .registeredAt(this.getRegisteredAt())
        .updatedAt(this.getUpdatedAt())
        .build();
  }
}
