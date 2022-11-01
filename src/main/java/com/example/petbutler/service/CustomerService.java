package com.example.petbutler.service;

import com.example.petbutler.dto.CustomerSignUpForm;
import com.example.petbutler.entity.Customer;
import com.example.petbutler.exception.ButlerUserException;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface CustomerService extends UserDetailsService {

  Customer signUpByEmail(CustomerSignUpForm customerSignUpForm) throws ButlerUserException;

  void emailAuth(String emailAuthKey);

  void sendEmailToUser(String email, String emailAuthKey, String mappingPath);

}
