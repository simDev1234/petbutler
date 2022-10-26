package com.example.petbutler.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
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
public class UserRegister {

  @NotBlank
  private String email;

  @NotBlank
  private String password;

  private String phone;

  @NotNull
  @Min(0)
  @Max(99)
  private int butlerLevel;

}
