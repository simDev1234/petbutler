package com.example.petbutler.service.impl;

import com.example.petbutler.dto.CustomerSignUpForm;
import com.example.petbutler.dto.PetDto;
import com.example.petbutler.entity.Customer;
import com.example.petbutler.entity.Pet;
import com.example.petbutler.exception.ButlerUserException;
import com.example.petbutler.exception.type.ErrorCode;
import com.example.petbutler.repository.CustomerRepository;
import com.example.petbutler.service.CustomerService;
import com.example.petbutler.type.UserRole;
import com.example.petbutler.utils.EmailSendUtils;
import com.example.petbutler.utils.FilePath;
import com.example.petbutler.utils.FileUploadUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailSendException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

  private final CustomerRepository customerRepository;

  private final EmailSendUtils emailSendUtils;



  /**
   * 고객 회원가입
   * - 반려동물이 등록되지 않아도 회원가입은 완료한다.
   * - 단, 회원가입이 완료되지 않으면 반려동물은 등록하지 않는다.
   */
  @Override
  @Transactional
  public Customer signUpByEmail(CustomerSignUpForm form)
                                throws ButlerUserException {

    validateSignUpByEmail(form.getEmail());

    // register customer
    Customer customer = customerRepository.save(Customer.from(form));

    // send auth email
    sendEmailToUser(customer.getEmail(), customer.getEmailAuthKey());

    return customer;
  }

  private void validateSignUpByEmail(String email) {

    Optional<Customer> optionalCustomer = customerRepository.findByEmail(email);

    if (optionalCustomer.isPresent()){
      throw new ButlerUserException(ErrorCode.USER_ALREADY_EXIST);
    }

  }

  /**
   * 이메일 전송
   */
  public void sendEmailToUser(String email, String emailAuthKey) throws MailSendException {

    String subject = "[팻집사] 회원가입에 감사드립니다.";
    String contents = emailSendUtils.getWelcomeHTML(emailAuthKey, UserRole.ROLE_CUSTOMER);

    emailSendUtils.sendMail(email, subject, contents);
  }

  /**
   * 계정 활성화
   */
  @Override
  @Transactional
  public void emailAuth(String emailAuthKey) {

    Customer customer = customerRepository.findByEmailAuthKey(emailAuthKey)
        .orElseThrow(() -> new ButlerUserException(ErrorCode.USER_NOT_FOUND));

    if (customer.isEmailAuthYn()) {
      throw new ButlerUserException(ErrorCode.USER_ALREADY_AUTHORIZED);
    }

    customer.authorize();

  }

  /**
   * 스프링 시큐러티 사용자 정보 조회 설정
   */
  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

    Customer customer = customerRepository.findByEmail(email)
        .orElseThrow(() -> new ButlerUserException(ErrorCode.USER_NOT_FOUND));
    
    return new User(customer.getEmail(), customer.getPassword(), Customer.getAuthorities(
        customer));

  }


}
