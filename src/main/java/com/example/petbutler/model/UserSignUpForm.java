package com.example.petbutler.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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

public class UserSignUpForm {

  @NotBlank
  private String email;

  @NotBlank
  private String password;

  private String phone;

  @NotNull
  private int butlerLevel;



}
