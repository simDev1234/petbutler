package com.example.petbutler.service.impl;

import com.example.petbutler.components.MailComponents;
import com.example.petbutler.domain.user.User;
import com.example.petbutler.dto.UserRegister;
import com.example.petbutler.exception.ButlerException;
import com.example.petbutler.repository.UserRepository;
import com.example.petbutler.service.UserService;
import com.example.petbutler.type.ErrorCode;
import com.example.petbutler.type.UserGrade;
import com.example.petbutler.type.UserRegisterType;
import com.example.petbutler.type.UserStatus;
import com.example.petbutler.utils.EmailSendUtil;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailSendException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  private final MailComponents mailComponents;

  /**
   * 이메일 가입하기
   * - 동일한 아이디가 있는 경우 실패 응답
   * - 회원 생성 후 인증 메일을 전송하며, 인증 메일 발송 중 예외 발생 시, 롤백 및 실패응답
   */
  @Override
  @Transactional(rollbackFor = ButlerException.class)
  public String registerByEmail(UserRegister request, String url) throws ButlerException{

    validateUserRegister(request);

    String encPassword = BCrypt.hashpw(request.getPassword(), BCrypt.gensalt());
    String emailAuthKey = UUID.randomUUID().toString().replace("-","");

    User newUser = userRepository.save(
        User.builder()
            .email(request.getEmail())
            .password(encPassword)
            .phone(request.getPhone())
            .butlerLevel(request.getButlerLevel())
            .emailAuthYn(false)
            .emailAuthKey(emailAuthKey)
            .userRegisterType(UserRegisterType.EMAIL)
            .userStatus(UserStatus.NOT_AUTHORIZED)
            .build()
    );

    String subject = "[팻집사] 회원가입에 감사드립니다.";
    String contents = EmailSendUtil.getWelcomeHTML(url, emailAuthKey);

    try {
      mailComponents.sendMail(request.getEmail(), subject, contents);
    } catch (MailSendException e) {
      throw new ButlerException(ErrorCode.EMAIL_SEND_ERROR);
    }

    return newUser.getEmail();
  }

  private void validateUserRegister(UserRegister request) {
    Optional<User> user = userRepository.findById(request.getEmail());

    if (user.isPresent()){
      throw new ButlerException(ErrorCode.USER_ALREADY_EXIST);
    }
  }

  /**
   * 계정 활성화
   * - 계정이 없는 경우, 이미 계정이 활성화된 경우, 실패 응답
   * - 이메일에서 링크를 통해 받은 인증키와 일치하는 아이디 활성화
   */
  @Override
  @Transactional
  public void emailAuth(String emailAuthKey) {

    User user = userRepository.findByEmailAuthKey(emailAuthKey)
        .orElseThrow(() -> new ButlerException(ErrorCode.USER_NOT_FOUND));

    if (user.isEmailAuthYn()) {
      throw new ButlerException(ErrorCode.USER_ALREADY_AUTHORIZED);
    }

    user.setEmailAuthYn(true);
    user.setEmailAuthAt(LocalDateTime.now());
    user.setUserStatus(UserStatus.IN_USE);

  }

  /**
   * 스프링 시큐러티 사용자 정보 로드
   */
  @Override
  public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {

    User user = userRepository.findById(userId)
        .orElseThrow(() -> new ButlerException(ErrorCode.USER_NOT_FOUND));

    validateLoadUserByUsername(user);

    // 사용자 권한, Role 추가
    List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

    grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));

    if (UserGrade.ADMIN.equals(user.getUserGrade())) { // 관리자일 경우
      grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }

    return new org.springframework.security.core.userdetails.User(
        user.getEmail(), user.getPassword(), grantedAuthorities);
  }

  private static void validateLoadUserByUsername(User user) {

    if (UserStatus.NOT_AUTHORIZED.equals(user.getUserStatus())) {
      throw new ButlerException(ErrorCode.USER_EMAIL_NOT_AUTHORIZED);
    }

    if (UserStatus.STOPPED.equals(user.getUserStatus())) {
      throw new ButlerException(ErrorCode.USER_STATUS_STOP);
    }

  }

}
