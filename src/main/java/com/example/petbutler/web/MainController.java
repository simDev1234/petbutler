package com.example.petbutler.web;

import com.example.petbutler.security.authentication.JwtTokenProvider;
import com.mysql.cj.util.StringUtils;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class MainController {

  private final JwtTokenProvider jwtTokenProvider;

  /**
   * 메인페이지 이동
   */
  @RequestMapping("/")
  public String index(HttpServletRequest request, Model model){

    String email = jwtTokenProvider.getEmail(request);

    if (!StringUtils.isNullOrEmpty(email)){
      model.addAttribute("email", email);
    }

    return "index";
  }


}
