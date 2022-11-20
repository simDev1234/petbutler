package com.example.petbutler.exception.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {

  // user (회원 관련 예외)
  USER_NOT_FOUND("해당 아이디가 존재하지 않습니다.", HttpStatus.BAD_REQUEST),

  USER_PASSWORD_NOT_MATCH("비밀번호가 틀렸습니다.", HttpStatus.BAD_REQUEST),

  USER_ALREADY_EXIST("이미 등록된 아이디입니다.", HttpStatus.BAD_REQUEST),

  USER_ALREADY_AUTHORIZED("이미 활성화된 아이디입니다.", HttpStatus.BAD_REQUEST),

  USER_EMAIL_NOT_AUTHORIZED("이메일 인증 후 로그인 해주세요.", HttpStatus.BAD_REQUEST),

  USER_STATUS_STOP("정지된 아이디입니다.", HttpStatus.BAD_REQUEST),

  USER_STATUS_WITHDRAW("탈퇴한 아이디입니다.", HttpStatus.BAD_REQUEST),

  USER_REGISTER_NOT_ENOUGH_DATA("필수 입력 사항이 입력되지 않았습니다.", HttpStatus.BAD_REQUEST),

  EMAIL_AUTH_TOKEN_EXPIRED("유효기간이 만료된 토큰입니다.", HttpStatus.BAD_REQUEST),

  EMAIL_SEND_ERROR("이메일 전송 중 문제가 발생했습니다. 다른 메일을 사용해주세요", HttpStatus.BAD_REQUEST),

  // pet (팻 관련 예외)
  PET_DATA_NOT_EXIST("팻 등록 정보가 정상적으로 전송되지 않았습니다.", HttpStatus.BAD_REQUEST),

  PET_NOT_FOUND("해당 팻이 존재하지 않습니다.", HttpStatus.BAD_REQUEST),

  // category (카테고리 관련 예외)
  INVALID_CATEGORY_DATA("카테고리 데이터가 유효하지 않습니다.", HttpStatus.BAD_REQUEST),

  // product (상품 관련 예외)
  PRODUCT_NOT_FOUND("해당 상품이 존재하지 않습니다.", HttpStatus.BAD_REQUEST),

  // api (동물 병원/동물 약국 관련 예외)
  API_JSON_PARSE_EXCEPTION("해당 카테고리에 재고가 있는 상품이 있습니다.", HttpStatus.BAD_REQUEST),

  API_DATA_ALREADY_EXIST("해당 데이터는 이미 등록된 데이터입니다.", HttpStatus.BAD_REQUEST),

  API_DATA_HOSPITAL_SHUTDOWN("해당 동물 병원은 폐쇄되었습니다.", HttpStatus.BAD_REQUEST),

  API_DATA_HOSPITAL_NOT_FOUND("해당 동물 병원을 찾을수 없습니다", HttpStatus.BAD_REQUEST),

  API_DATA_HOSPITAL_HAS_NO_PRODUCTS("해당 동물 병원에 등록된 상품이 없습니다.", HttpStatus.BAD_REQUEST),

  // etc (기타 예외 발생 시)
  INTERNAL_SERVER_ERROR("요청 중 서버 내부에 문제가 발생했습니다.", HttpStatus.BAD_REQUEST),

  INVALID_REQUEST("요청 사항이 유효하지 않습니다.", HttpStatus.BAD_REQUEST),

  CATEGORY_ALREADY_EXIST("카테고리가 이미 있습니다.", HttpStatus.BAD_REQUEST),

  CATEGORY_DIVISION_FULL("해당 분류에 카테고리가 모두 찼습니다.", HttpStatus.BAD_REQUEST),

  CATEGORY_NOT_FOUND("해당 카테고리가 존재하지 않습니다.", HttpStatus.BAD_REQUEST),

  CATEGORY_HAS_PRODUCT("해당 카테고리에 재고가 있는 상품이 있습니다.", HttpStatus.BAD_REQUEST);


  private String message;

  private HttpStatus httpStatus;

}
