package com.example.petbutler.service.impl;

import com.example.petbutler.exception.ButlerUserException;
import com.example.petbutler.exception.constants.ErrorCode;
import com.example.petbutler.model.PetRegisterForm;
import com.example.petbutler.model.UserDetailForm;
import com.example.petbutler.model.UserSignInForm;
import com.example.petbutler.model.UserSignUpForm;
import com.example.petbutler.model.constants.UserStatus;
import com.example.petbutler.persist.UserRepository;
import com.example.petbutler.persist.entity.User;
import com.example.petbutler.service.UserService;
import com.example.petbutler.utils.EmailSendUtils;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  private final PasswordEncoder passwordEncoder;

  private final EmailSendUtils emailSendUtils;

  /**
   * 로그인 시 아이디, 비밀번호 일치 확인
   */
  @Override
  public User authenticate(UserSignInForm userSignInForm) {

    User user = userRepository.findByEmail(userSignInForm.getEmail())
        .orElseThrow(() -> new ButlerUserException(ErrorCode.USER_NOT_FOUND));

    if (!passwordEncoder.matches(userSignInForm.getPassword(), user.getPassword())) {
      new ButlerUserException(ErrorCode.USER_PASSWORD_NOT_MATCH);
    }

    return user;
  }

  /**
   * 고객 회원가입
   * - 이미 가입한 회원인 경우 실패 응답
   * - 회원 가입 절차
   *    (1) 회원 등록
   *    (2) 인증 메일 전송
   *    (3) 반려 동물 등록
   */
  @Override
  @Transactional
  public User signUpByEmail(UserSignUpForm userSignUpForm, PetRegisterForm petRegisterForm) {

    // 이미 가입한 회원인지 확인
    validateIfEmailAlreadyRegistered(userSignUpForm.getEmail());

    // 회원 등록
    User user = userSignUpForm.toEntity();

    user.setPassword(passwordEncoder.encode(user.getPassword()));

    user = userRepository.save(user);

    // 인증 메일 전송
    sendEmailToUser(user.getEmail(), user.getEmailAuthKey());

    return user;
  }

  private void validateIfEmailAlreadyRegistered(String email) {

    boolean exists = userRepository.existsByEmail(email);

    if (exists){
      throw new ButlerUserException(ErrorCode.USER_ALREADY_EXIST);
    }

  }

  /**
   * 인증 메일 전송
   */
  public void sendEmailToUser(String email, String emailAuthKey){

    String subject = "[팻집사] 회원가입에 감사드립니다.";
    String contents = emailSendUtils.getWelcomeHTML(email, emailAuthKey);

    emailSendUtils.sendMail(email, subject, contents);
  }

  /**
   * 계정 활성화
   * - 해당 토큰을 가진 사용자를 찾을 수 없는 경우, 정지 또는 탈퇴 상태인 경우,
   * - 메일 인증 토큰 발행 시기가 하루가 지난 경우, 실패 응답
   * - 전달 받은 인증 토큰
   */
  @Override
  @Transactional
  public void emailAuth(String email, String emailAuthKey) {

    User user = userRepository.findByEmailAndEmailAuthKey(email, emailAuthKey)
        .orElseThrow(() -> new ButlerUserException(ErrorCode.USER_NOT_FOUND));

    validateBeforeEmailAuth(user);

    user.authorize();

  }

  private static void validateBeforeEmailAuth(User user) {

    validateIfUserStatusStoppedOrWithdraw(user);

    if (user.isEmailAuthYn() || UserStatus.IN_USE.equals(user.getUserStatus())) {
      throw new ButlerUserException(ErrorCode.USER_ALREADY_AUTHORIZED);
    }

    LocalDateTime today = LocalDateTime.now();

    if (user.getEmailAuthExpiredAt().isBefore(today.minusDays(1))) {
      throw new ButlerUserException(ErrorCode.EMAIL_AUTH_TOKEN_EXPIRED);
    }

  }

  private static void validateIfUserStatusStoppedOrWithdraw(User user) {

    // 정지상태일 때
    if (UserStatus.STOPPED.equals(user.getUserStatus())) {
      throw new ButlerUserException(ErrorCode.USER_STATUS_STOP);
    }

    // 탈퇴상태일 때
    if (UserStatus.WITHDRAW.equals(user.getUserStatus())) {
      throw new ButlerUserException(ErrorCode.USER_STATUS_WITHDRAW);
    }
  }


  /**
   * 스프링 시큐러티 loadUserByUsername
   */
  @Override
  public UserDetails loadUserByUsername(String email) throws ButlerUserException {

    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new ButlerUserException(ErrorCode.USER_NOT_FOUND));

    // 이메일 인증 및 정지/탈퇴 등 회원 상태 확인
    validateIfUserIsInUse(user);

    // 로그인 정보 로드
    return user;

  }

  private static void validateIfUserIsInUse(User user) {

    if (UserStatus.NOT_AUTHORIZED.equals(user.getUserStatus())) {
      throw new ButlerUserException(ErrorCode.USER_EMAIL_NOT_AUTHORIZED);
    }

    validateIfUserStatusStoppedOrWithdraw(user);
  }

  /**
   * 회원 조회
   * - 회원이 존재하지 않는 경우 실패 응답
   */
  @Override
  @Transactional
  public UserDetailForm getUserDetailByEmail(String email) {

    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new ButlerUserException(ErrorCode.USER_NOT_FOUND));

    return UserDetailForm.builder()
        .butlerLevel(user.getButlerLevel())
        .email(user.getEmail())
        .phone(user.getPhone())
        .build();
  }

  /**
   * 회원 정보 수정
   * - 회원 정보가 없는 경우 실패 응답
   * - 집사레벨, 핸드폰 번호 수정
   */
  @Override
  @Transactional
  public void updateUser(UserDetailForm form) {

    User user = userRepository.findByEmail(form.getEmail())
        .orElseThrow(() -> new ButlerUserException(ErrorCode.USER_NOT_FOUND));

    user.setButlerLevel(form.getButlerLevel());
    user.setPhone(form.getPhone());
  }

  /**
   * 회원 탈퇴
   * - 회원 정보가 없는 경우 실패 응답
   * - 물리 삭제가 아닌 상태 정보 수정
   */
  @Override
  @Transactional
  public void withdraw(String email) {

    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new ButlerUserException(ErrorCode.USER_NOT_FOUND));

    user.setUserStatus(UserStatus.WITHDRAW);
    
  }



}
