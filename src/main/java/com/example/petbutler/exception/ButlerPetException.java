package com.example.petbutler.exception;

import com.example.petbutler.exception.type.ErrorCode;
import lombok.Getter;

@Getter
public class ButlerPetException extends RuntimeException {

  private final ErrorCode errorCode;

  public ButlerPetException(ErrorCode errorCode) {
    this.errorCode = errorCode;
  }

  public ButlerPetException(String message, ErrorCode errorCode) {
    super(message);
    this.errorCode = errorCode;
  }

  public ButlerPetException(String message, Throwable cause, ErrorCode errorCode) {
    super(message, cause);
    this.errorCode = errorCode;
  }

  public ButlerPetException(Throwable cause, ErrorCode errorCode) {
    super(cause);
    this.errorCode = errorCode;
  }

  public ButlerPetException(String message, Throwable cause, boolean enableSuppression,
    boolean writableStackTrace, ErrorCode errorCode) {
    super(message, cause, enableSuppression, writableStackTrace);
    this.errorCode = errorCode;
  }
}
