package com.example.petbutler.service.impl;

import com.example.petbutler.exception.ButlerUserException;
import com.example.petbutler.exception.type.ErrorCode;
import com.example.petbutler.model.PetRegisterForm;
import com.example.petbutler.model.UserSearch;
import com.example.petbutler.model.UserSignUpForm;
import com.example.petbutler.persist.UserRepository;
import com.example.petbutler.persist.entity.User;
import com.example.petbutler.service.UserService;
import com.example.petbutler.type.UserRole;
import com.example.petbutler.type.UserStatus;
import com.example.petbutler.utils.EmailSendUtils;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  private final EmailSendUtils emailSendUtils;

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
    User user = userRepository.save(User.of(userSignUpForm));

    // 인증 메일 전송
    sendEmailToUser(user.getEmail(), user.getEmailAuthKey());

    return user;
  }

  private void validateIfEmailAlreadyRegistered(String email) {

    Optional<User> optionalCustomer = userRepository.findByEmail(email);

    if (optionalCustomer.isPresent()){
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
   * 스프링 시큐러티 사용자 정보 로드
   */
  @Override
  public UserDetails loadUserByUsername(String email) throws ButlerUserException {

    var user = userRepository.findByEmail(email)
        .orElseThrow(() -> new ButlerUserException(ErrorCode.USER_NOT_FOUND));

    validateIfUserIsInUse(user);

    List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();

    grantedAuthorityList.add(new SimpleGrantedAuthority(UserRole.ROLE_REGULAR.name()));

    if (UserRole.ROLE_ADMIN.equals(user.getUserRole())) {
      grantedAuthorityList.add(new SimpleGrantedAuthority(UserRole.ROLE_ADMIN.name()));
    }

    return new org.springframework.security.core.userdetails.User(
        user.getEmail(), user.getPassword(), grantedAuthorityList
    );
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
  public UserSearch getUserDetailByEmail(String email) {

    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new ButlerUserException(ErrorCode.USER_NOT_FOUND));

    return UserSearch.builder()
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
  public void updateUser(String email, UserSearch detail) {

    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new ButlerUserException(ErrorCode.USER_NOT_FOUND));

    user.setButlerLevel(detail.getButlerLevel());
    user.setPhone(detail.getPhone());
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
