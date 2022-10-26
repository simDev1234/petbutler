package com.example.petbutler.exception;

import static com.example.petbutler.type.ErrorCode.INTERNAL_SERVER_ERROR;
import static com.example.petbutler.type.ErrorCode.INVALID_REQUEST;

import com.example.petbutler.type.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  @ExceptionHandler(ButlerException.class)
  public ErrorResponse handleButlerException(ButlerException e) {

    ErrorCode errorCode = e.getErrorCode();

    log.error("{} is occured.", errorCode.name());

    return new ErrorResponse(errorCode.name(), errorCode.getMessage());
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ErrorResponse handleDataIntegrityViolationException(DataIntegrityViolationException e) {
    log.error("DataIntegrityViolationException is occured.", e);

    return new ErrorResponse(INVALID_REQUEST.name(), INVALID_REQUEST.getMessage());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
    log.error("MethodArgumentNotValidException is occured.", e);

    return new ErrorResponse(INVALID_REQUEST.name(), INVALID_REQUEST.getMessage());
  }

  @ExceptionHandler(Exception.class)
  public ErrorResponse handleException(Exception e) {
    log.error("Exception is occured.", e);

    return new ErrorResponse(INTERNAL_SERVER_ERROR.name(), INTERNAL_SERVER_ERROR.getMessage());
  }

}
