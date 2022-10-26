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
   * - 로그인 정보 있을 경우 메뉴에 '마이페이지' 노출
   * - 로그인 정보 없을 경우 '로그인/회원가입' 노출
   */
  @GetMapping("/")
  public String index(Principal principal, Model model){

    if (Objects.nonNull(principal)){
      model.addAttribute("login", true);
    }

    return "index";
  }

}
