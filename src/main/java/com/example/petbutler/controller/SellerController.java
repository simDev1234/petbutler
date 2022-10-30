package com.example.petbutler.controller;

import com.example.petbutler.dto.SellerSignUpForm;
import com.example.petbutler.service.SellerService;
import com.example.petbutler.type.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class SellerController {

  private final SellerService sellerService;

  /**
   * 회원가입 페이지 이동
   */
  @GetMapping("/users/seller/sign-up")
  public String getSignUpPage() {

    return "users/seller/sign-up";

  }

  /**
   * 회원 가입
   */
  @PostMapping(value = "/users/seller/sign-up")
  public String signUpByEmail(SellerSignUpForm form, Model model) {

    model.addAttribute(sellerService.signUpByEmail(form));

    return "users/sign-up-complete";
  }

  /**
   * 이메일 인증
   */
  @GetMapping("/users/seller/email-auth")
  public String authorizeUserByEmail(@RequestParam(name = "authKey") String emailAuthKey) {

    sellerService.emailAuth(emailAuthKey);

    return "users/sign-in";

  }

}
