package com.example.petbutler.service;

import com.example.petbutler.model.UserDetailForm;
import com.example.petbutler.model.UserPasswordResetForm;
import com.example.petbutler.model.UserSignInForm;
import com.example.petbutler.model.UserSignUpForm;
import com.example.petbutler.model.PetRegisterForm;
import com.example.petbutler.persist.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

  User signUpByEmail(UserSignUpForm userSignUpForm, PetRegisterForm petRegisterForm);

  void emailAuth(String email, String emailAuthKey);

  UserDetails loadUserByUsername(String username);

  UserDetailForm getUserDetailByEmail(String email);

  void updateUser(UserDetailForm form);

  void sendPasswordResetPage(String email);

  void passwordReset(UserPasswordResetForm form);

  void withdraw(String email);

  User authenticate(UserSignInForm userSignInForm);
}
