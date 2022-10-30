package com.example.petbutler.entity;

import com.example.petbutler.dto.SellerSignUpForm;
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
public class Seller extends BaseEntity{

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String email;

  private UserRole userRole;

  private String password;

  private String name;

  private String phone;

  private String company;

  private String department;

  private String address;

  private String addressDetail;

  private boolean emailAuthYn;
  private String emailAuthKey;
  private LocalDateTime emailAuthExpiredAt;

  @Enumerated(value = EnumType.STRING)
  private UserStatus userStatus;

  public static Seller from(SellerSignUpForm form) {
    return Seller.builder()
        .email(form.getEmail().toLowerCase(Locale.ROOT))
        .password(BCrypt.hashpw(form.getPassword(), BCrypt.gensalt()))
        .userRole(UserRole.ROLE_SELLER)
        .userStatus(UserStatus.NOT_AUTHORIZED)
        .name(form.getName())
        .company(form.getCompany())
        .department(form.getDepartment())
        .address(form.getAddress())
        .addressDetail(form.getAddressDetail())
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

  public static List<GrantedAuthority> getAuthorities(Seller seller){
    List<GrantedAuthority> authorities = new ArrayList<>();

    authorities.add(new SimpleGrantedAuthority(seller.userRole.name()));

    return authorities;
  }

}
