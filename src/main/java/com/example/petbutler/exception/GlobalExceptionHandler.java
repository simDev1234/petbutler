package com.example.petbutler.exception;

import com.example.petbutler.exception.ButlerCategoryException;
import com.example.petbutler.exception.ButlerUserException;
import com.example.petbutler.exception.constants.ErrorCode;
import com.example.petbutler.exception.constants.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSendException;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(ButlerUserException.class)
  public String handleButlerUserException(ButlerUserException e, Model model) {

    ErrorCode errorCode = e.getErrorCode();

    log.error("{} is occured.", errorCode.name());

    model.addAttribute("errorResponse",
        new ErrorResponse(errorCode.name(), errorCode.getMessage()));

    return "exception/error-msg";
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(ButlerApiException.class)
  public String handleButlerApiException(ButlerApiException e, Model model) {

    ErrorCode errorCode = e.getErrorCode();

    log.error("{} is occured.", errorCode.name());

    model.addAttribute("errorResponse",
        new ErrorResponse(errorCode.name(), errorCode.getMessage()));

    return "exception/error-msg";
  }

  @ExceptionHandler(ButlerCategoryException.class)
  public ResponseEntity handleButlerCategoryException(ButlerCategoryException e) {

    ErrorCode errorCode = e.getErrorCode();

    log.error("{} is occured.", errorCode.name());

    return new ResponseEntity(new ErrorResponse(errorCode.name(), errorCode.getMessage()), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public String handleDataIntegrityViolationException(DataIntegrityViolationException e, Model model) {

    log.error("DataIntegrityViolationException is occured.", e);

    model.addAttribute("errorResponse",
        new ErrorResponse("DataIntegrityViolationException", e.getMessage()));

    return "exception/error-msg";
  }

  @ExceptionHandler(MailSendException.class)
  public String handleMailSendException(MailSendException e, Model model){

    log.error("MailSendException is occured.", e);

    model.addAttribute("errorResponse",
        new ErrorResponse("MailSendException", e.getMessage()));

    return "exception/error-msg";
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public String handleMethodArgumentNotValidException(MethodArgumentNotValidException e, Model model) {

    log.error("MethodArgumentNotValidException is occured.", e);

    model.addAttribute("errorResponse",
        new ErrorResponse("MethodArgumentNotValidException", e.getMessage()));

    return "exception/error-msg";
  }

  @ExceptionHandler(Exception.class)
  public String handleException(Exception e, Model model) {

    log.error("Exception is occured.", e);

    model.addAttribute("errorResponse",
        new ErrorResponse("Exception", e.getMessage()));

    return "exception/error-msg";
  }

}
