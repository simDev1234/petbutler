package com.example.petbutler.service;

import com.example.petbutler.dto.SellerSignUpForm;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface SellerService extends UserDetailsService {


  String signUpByEmail(SellerSignUpForm form);

  void emailAuth(String emailAuthKey);

  void sendEmailToUser(String email, String emailAuthKey);
}
