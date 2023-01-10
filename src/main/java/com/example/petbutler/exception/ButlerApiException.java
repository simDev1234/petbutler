package com.example.petbutler.exception;

import com.example.petbutler.exception.constants.ErrorCode;
import lombok.Getter;

@Getter
public class ButlerApiException extends RuntimeException {

  private final ErrorCode errorCode;

  public ButlerApiException(ErrorCode errorCode) {
    this.errorCode = errorCode;
  }

  public ButlerApiException(String message, ErrorCode errorCode) {
    super(message);
    this.errorCode = errorCode;
  }

  public ButlerApiException(String message, Throwable cause, ErrorCode errorCode) {
    super(message, cause);
    this.errorCode = errorCode;
  }

  public ButlerApiException(Throwable cause, ErrorCode errorCode) {
    super(cause);
    this.errorCode = errorCode;
  }

  public ButlerApiException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace, ErrorCode errorCode) {
    super(message, cause, enableSuppression, writableStackTrace);
    this.errorCode = errorCode;
  }

}
