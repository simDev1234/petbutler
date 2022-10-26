package com.example.petbutler.controller;

import com.example.petbutler.dto.UserRegister;
import com.example.petbutler.service.UserService;
import java.security.Principal;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  /**
   *  로그인 페이지 이동
   */
  @GetMapping("/users/sign-in")
  public String signIn(){
    return "user/sign-in";
  }

  /**
   *  회원가입 페이지 이동
   */
  @GetMapping("/users/sign-up")
  public String signUp(){
    return "user/sign-up-email";
  }

  /**
   * 회원 가입
   */
  @PostMapping("/users/sign-up")
  public String signIn(UserRegister request, @RequestParam String registerType,
                       HttpServletRequest servletRequest, Model model){

    // 이메일 회원가입
    if ("email".equalsIgnoreCase(registerType)){
      // url (ip and port) for email sending
      String url = servletRequest.getRequestURL().toString().split(servletRequest.getRequestURI())[0];
      System.out.println(url);
      model.addAttribute("email", userService.registerByEmail(request, url));
      return "user/sign-up-email-complete";
    }

    // 구글 회원가입
//    if ("google".equalsIgnoreCase(registerType)){
//      model.addAttribute("userId", userService.registerByEmail(registerRequest));
//    }

    return "user/sign-up-step-two";
  }

  /**
   * 이메일 인증
   */
  @GetMapping("/users/email-auth")
  public String emailAuth(@RequestParam(name = "authKey") String emailAuthKey){

    userService.emailAuth(emailAuthKey);

    return "user/sign-up-step-two";
  }

  /**
   * 반려동물 등록
   */
  @PostMapping("/users/register-pets")
  public String registerPets(Principal principal){

    String userId = principal.getName();



    return null;
  }

}
