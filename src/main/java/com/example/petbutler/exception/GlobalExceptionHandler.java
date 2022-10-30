package com.example.petbutler.exception;

import static com.example.petbutler.exception.type.ErrorCode.INTERNAL_SERVER_ERROR;
import static com.example.petbutler.exception.type.ErrorCode.INVALID_REQUEST;

import com.example.petbutler.exception.type.ErrorCode;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailSendException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(ButlerUserException.class)
  public ErrorResponse handleButlerException(ButlerUserException e) {

    ErrorCode errorCode = e.getErrorCode();

    log.error("{} is occured.", errorCode.name());

    return new ErrorResponse(errorCode.name(), errorCode.getMessage());
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ErrorResponse handleDataIntegrityViolationException(DataIntegrityViolationException e) {
    log.error("DataIntegrityViolationException is occured.", e);

    return new ErrorResponse(INVALID_REQUEST.name(), INVALID_REQUEST.getMessage());
  }

  @ExceptionHandler(IOException.class)
  public ErrorResponse handleIOException(IOException e) {
    log.error("IOException is occured.", e);

    return new ErrorResponse(ErrorCode.FILE_CONTENTS_NOT_EXIST.name(), INVALID_REQUEST.getMessage());
  }

  @ExceptionHandler(MailSendException.class)
  public ErrorResponse handleMailSendException(MailSendException e){
    log.error("MailSendException is occured.", e);

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
