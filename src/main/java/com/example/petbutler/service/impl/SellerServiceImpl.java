package com.example.petbutler.service.impl;

import com.example.petbutler.dto.SellerSignUpForm;
import com.example.petbutler.entity.Seller;
import com.example.petbutler.exception.ButlerUserException;
import com.example.petbutler.exception.type.ErrorCode;
import com.example.petbutler.repository.SellerRepository;
import com.example.petbutler.service.SellerService;
import com.example.petbutler.type.UserRole;
import com.example.petbutler.utils.EmailSendUtils;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailSendException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.userdetails.User;

@Service
@RequiredArgsConstructor
public class SellerServiceImpl implements SellerService {

  private final SellerRepository sellerRepository;

  private final EmailSendUtils emailSendUtils;

  /**
   * 셀러 회원가입
   */
  @Override
  @Transactional(rollbackFor = ButlerUserException.class)
  public String signUpByEmail(SellerSignUpForm form) throws ButlerUserException {

    Optional<Seller> optionalSeller = sellerRepository.findByEmail(form.getEmail());

    if (optionalSeller.isPresent()){
      throw new ButlerUserException(ErrorCode.USER_ALREADY_EXIST);
    }

    Seller seller = sellerRepository.save(Seller.from(form));

    sendEmailToUser(seller.getEmail(), seller.getEmailAuthKey());

    return seller.getEmail();
  }

  /**
   * 이메일 전송
   */
  public void sendEmailToUser(String email, String emailAuthKey) throws MailSendException {

    String subject = "[팻집사] 회원가입에 감사드립니다.";
    String contents = emailSendUtils.getWelcomeHTML(emailAuthKey, UserRole.ROLE_SELLER);

    emailSendUtils.sendMail(email, subject, contents);
  }

  /**
   * 계정 활성화
   */
  @Override
  @Transactional
  public void emailAuth(String emailAuthKey) {

    Seller seller = sellerRepository.findByEmailAuthKey(emailAuthKey)
        .orElseThrow(() -> new ButlerUserException(ErrorCode.USER_NOT_FOUND));

    if (seller.isEmailAuthYn()) {
      throw new ButlerUserException(ErrorCode.USER_ALREADY_AUTHORIZED);
    }

    seller.authorize();

  }

  /**
   * 스프링 시큐러티 사용자 정보 조회 설정
   */
  @Override
  public UserDetails loadUserByUsername(String email) throws ButlerUserException {

    Seller seller = sellerRepository.findByEmail(email)
        .orElseThrow(() -> new ButlerUserException(ErrorCode.USER_NOT_FOUND));
    
    return new User(seller.getEmail(), seller.getPassword(), Seller.getAuthorities(seller));

  }


}
