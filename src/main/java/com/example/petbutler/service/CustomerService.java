package com.example.petbutler.service;

import com.example.petbutler.dto.CustomerSignUpForm;
import com.example.petbutler.dto.PetDto;
import com.example.petbutler.entity.Customer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

public interface CustomerService extends UserDetailsService {

  Customer signUpByEmail(CustomerSignUpForm form, PetDto[] petDtos, MultipartFile[] files);

  void emailAuth(String emailAuthKey);

  void sendEmailToUser(String email, String emailAuthKey, String mappingPath);

}
