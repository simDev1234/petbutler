package com.example.petbutler.exception;

import com.example.petbutler.type.ErrorCode;
import lombok.Getter;

@Getter
public class ButlerException extends RuntimeException {

  private final ErrorCode errorCode;

  public ButlerException(ErrorCode errorCode) {
    this.errorCode = errorCode;
  }

  public ButlerException(String message, ErrorCode errorCode) {
    super(message);
    this.errorCode = errorCode;
  }

  public ButlerException(String message, Throwable cause, ErrorCode errorCode) {
    super(message, cause);
    this.errorCode = errorCode;
  }

  public ButlerException(Throwable cause, ErrorCode errorCode) {
    super(cause);
    this.errorCode = errorCode;
  }

  public ButlerException(String message, Throwable cause, boolean enableSuppression,
    boolean writableStackTrace, ErrorCode errorCode) {
    super(message, cause, enableSuppression, writableStackTrace);
    this.errorCode = errorCode;
  }
}
