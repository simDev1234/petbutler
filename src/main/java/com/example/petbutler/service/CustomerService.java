package com.example.petbutler.service;

import com.example.petbutler.dto.CustomerSignUpForm;
import com.example.petbutler.entity.Customer;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface CustomerService extends UserDetailsService {

  Customer signUpByEmail(CustomerSignUpForm form);

  void emailAuth(String emailAuthKey);

  void sendEmailToUser(String email, String emailAuthKey);

}
