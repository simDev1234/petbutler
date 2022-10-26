package com.example.petbutler.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {

  USER_NOT_FOUND("해당 아이디가 존재하지 않습니다.", HttpStatus.BAD_REQUEST),
  USER_ALREADY_EXIST("이미 등록된 아이디입니다.", HttpStatus.BAD_REQUEST),
  USER_ALREADY_AUTHORIZED("해당 이메일의 아이디는 이미 인증 후 활성화되었습니다.", HttpStatus.BAD_REQUEST),
  USER_EMAIL_NOT_AUTHORIZED("이메일 인증 후 로그인 해주세요.", HttpStatus.BAD_REQUEST),
  USER_STATUS_STOP("정지된 아이디입니다.", HttpStatus.BAD_REQUEST),
  EMAIL_SEND_ERROR("이메일 전송 중 문제가 발생했습니다. 다른 메일을 사용해주세요", HttpStatus.BAD_REQUEST),
  INVALID_REQUEST("요청 사항이 유효하지 않습니다.", HttpStatus.BAD_REQUEST),
  INTERNAL_SERVER_ERROR("요청 중 서버 내부에 문제가 발생했습니다.", HttpStatus.BAD_REQUEST);

  private String message;
  private HttpStatus httpStatus;

}
