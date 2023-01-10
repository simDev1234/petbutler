package com.example.petbutler.web;

import com.example.petbutler.model.PetRegisterForm;
import com.example.petbutler.model.UserDetailForm;
import com.example.petbutler.model.UserPasswordResetForm;
import com.example.petbutler.model.UserSignInForm;
import com.example.petbutler.model.UserSignUpForm;
import com.example.petbutler.persist.entity.User;
import com.example.petbutler.security.authentication.JwtTokenProvider;
import com.example.petbutler.service.PetService;
import com.example.petbutler.service.UserService;
import java.security.Principal;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  private final PetService petService;

  private final JwtTokenProvider jwtTokenProvider;

  /**
   * 로그인 페이지 이동
   */
  @GetMapping("/sign-in")
  public String getSignInPage() {

    return "user/sign-in";

  }

  /**
   * 로그인 실패 시 페이지 이동
   */
  @GetMapping("/sign-in/fail")
  public String getSignInFailPage(){
    return "user/sign-in-fail";
  }

  /**
   * 로그인 (JWT 인증 토큰 생성)
   */
  @PostMapping("/sign-in")
  public String signIn(UserSignInForm userSignInForm, HttpServletResponse response){

    // 아이디와 비밀번호 확인
    User user = userService.authenticate(userSignInForm);

    // JWT 토큰 생성
    String token = jwtTokenProvider.generateToken(user.getEmail(), user.getUserRole());

    // 응답 헤더의 쿠키에 HttpOnly로 토큰 저장
    String cookieValue = String.format("%s%s", JwtTokenProvider.TOKEN_PREFIX, token);
    Cookie cookie = new Cookie(JwtTokenProvider.TOKEN_HEADER, cookieValue);
    cookie.setPath("/");
    cookie.setMaxAge((int)JwtTokenProvider.TOKEN_EXPIRATION_TIME);
    cookie.setHttpOnly(true); // script로부터 접근 불가
    response.addCookie(cookie);

    // Authentication 추출
    Authentication authentication = jwtTokenProvider.getAuthentication(token);

    // SecurityContext에 등록
    SecurityContextHolder.getContext().setAuthentication(authentication);

    return "redirect:/";
  }

  /**
   * 비밀번호 찾기 페이지 이동
   */
  @GetMapping("/find-password")
  public String getFindPasswordPage(){
    return "user/password-find";
  }

  /**
   * 비밀번호 변경 이메일 전송
   */
  @GetMapping("/reset-email")
  public String sendPasswordResetPage(@RequestParam String email, Model model){

    userService.sendPasswordResetPage(email);

    String msg = String.format("%s 로 비밀번호 변경 링크를 전송하였습니다. 해당 링크를 통해 비밀번호를 재설정해주세요.", email);

    model.addAttribute("msg", msg);

    return "user/password-complete-msg";
  }

  /**
   * 비밀번호 변경 페이지 이동
   */
  @GetMapping("/reset-password")
  public String getResetPasswordPage(@RequestParam String email, Model model){

    model.addAttribute("email", email);

    return "user/password-reset";
  }

  /**
   * 비밀번호 변경
   */
  @PostMapping("/reset-password")
  public String resetPassword(UserPasswordResetForm form, Model model) {

    userService.passwordReset(form);

    String msg = "비밀번호가 정성적으로 변경되었습니다. 다시 로그인 해주세요.";

    model.addAttribute("msg", msg);

    return "user/password-complete-msg";
  }


  /**
   * 회원가입 페이지 이동
   */
  @GetMapping("/sign-up")
  public String getSignUpPage() {

    return "user/sign-up";

  }

  /**
   * 회원 가입
   */
  @PostMapping(value = "/sign-up", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public String signUpByEmail(UserSignUpForm userSignUpForm,
                              PetRegisterForm petRegisterForm, Model model) {

    // 이메일 회원가입
    User user = userService.signUpByEmail(userSignUpForm, petRegisterForm);

    // 팻 등록
    petService.registerPetsWhenSignUp(user, petRegisterForm);

    model.addAttribute("email", user.getEmail());

    return "user/sign-up-complete";

  }

  /**
   * 이메일 인증
   */
  @GetMapping("/email-auth")
  public String authorizeUserByEmail(@RequestParam(name = "email") String email,
                                     @RequestParam(name = "auth-key") String emailAuthKey) {

    userService.emailAuth(email, emailAuthKey);

    return "redirect:/user/sign-in";

  }

  /**
   * 마이페이지 - 회원 조회
   */
  @GetMapping("/mypage")
  public String getMyPage(Principal principal, Model model) {

    // Principal에서 아이디 추출
    String email = principal.getName();

    // 회원 정보
    UserDetailForm user = userService.getUserDetailByEmail(email);

    model.addAttribute("user", user);

    return "user/mypage";
  }

  /**
   * 마이페이지 - 회원수정
   */
  @PutMapping(value = "/mypage", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> updateUser(@RequestBody UserDetailForm form){

    userService.updateUser(form);

    return ResponseEntity.ok().build();
  }

  /**
   * 마이페이지 - 회원탈퇴
   * - 물리삭제 아닌 플래그 처리
   */
  @DeleteMapping("/withdraw/{email}")
  public ResponseEntity<?> deleteCustomer(@PathVariable String email, HttpServletResponse response){

    // 회원 상태 탈퇴 처리
    userService.withdraw(email);

    // 쿠키 삭제
    Cookie cookie = new Cookie(JwtTokenProvider.TOKEN_HEADER, null);
    cookie.setMaxAge(0);
    cookie.setPath("/");
    response.addCookie(cookie);

    return ResponseEntity.ok().build();
  }

  /**
   * 로그아웃
   */
  @RequestMapping("/logout")
  public String logout(HttpServletResponse response){

    // 쿠키 삭제
    Cookie cookie = new Cookie(JwtTokenProvider.TOKEN_HEADER, null);
    cookie.setMaxAge(0);
    cookie.setPath("/");
    response.addCookie(cookie);

    return "redirect:/";
  }

}
