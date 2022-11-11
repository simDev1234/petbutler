package com.example.petbutler.controller;

import java.security.Principal;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class MainController {

  /**
   * 메인페이지 이동
   */
  @RequestMapping("/")
  public String index(Principal principal, Model model){

    if (principal != null) {
      model.addAttribute("email", principal.getName());
    }

    return "index";
  }



}
