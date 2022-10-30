package com.example.petbutler.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorResponse {
  private String code;
  private String message;

}
