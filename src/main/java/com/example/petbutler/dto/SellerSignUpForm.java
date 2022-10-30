package com.example.petbutler.dto;

import com.example.petbutler.type.UserRole;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SellerSignUpForm implements Serializable {

  private String email;

  private String password;

  private String phone;

  private UserRole role;

  private String name;

  private String company;

  private String department;

  private String address;

  private String addressDetail;

}
