package com.example.petbutler.service;

import com.example.petbutler.dto.UserRegister;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

  String registerByEmail(UserRegister request, String url);

  void emailAuth(String emailAuthKey);
}
