package com.example.petbutler.entity;

import com.example.petbutler.dto.CustomerSignUpForm;
import com.example.petbutler.type.UserRole;
import com.example.petbutler.type.UserStatus;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCrypt;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@AuditOverride(forClass = BaseEntity.class)
public class Customer extends BaseEntity{

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String email;

  private UserRole userRole;

  private String password;

  private int butlerLevel;

  private String phone;

  private boolean emailAuthYn;
  private String emailAuthKey;
  private LocalDateTime emailAuthExpiredAt;

  @Enumerated(value = EnumType.STRING)
  private UserStatus userStatus;

  public static Customer from(CustomerSignUpForm form) {
    return Customer.builder()
        .email(form.getEmail().toLowerCase(Locale.ROOT))
        .password(BCrypt.hashpw(form.getPassword(), BCrypt.gensalt()))
        .butlerLevel(form.getButlerLevel())
        .userRole(UserRole.ROLE_CUSTOMER)
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
  }

  public static List<GrantedAuthority> getAuthorities(Customer member){
    List<GrantedAuthority> authorities = new ArrayList<>();

    authorities.add(new SimpleGrantedAuthority(member.userRole.name()));

    return authorities;
  }

}
