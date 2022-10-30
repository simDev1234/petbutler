package com.example.petbutler.controller;

import java.security.Principal;
import java.util.Objects;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

  /**
   * 메인페이지 이동
   */
  @GetMapping("/")
  public String index(Principal principal, Model model){

    if (Objects.nonNull(principal)){
      model.addAttribute("login", true);
    }

    return "index";
  }

  /**
   * 로그인 페이지 이동
   */
  @GetMapping("/users/sign-in")
  public String getSignInPage() {

    return "users/sign-in";

  }

}
