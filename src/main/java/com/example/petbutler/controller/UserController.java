package com.example.petbutler.controller;

import com.example.petbutler.model.PetRegisterForm;
import com.example.petbutler.model.UserSearch;
import com.example.petbutler.model.UserSignUpForm;
import com.example.petbutler.persist.entity.User;
import com.example.petbutler.security.authentication.JwtTokenUtils;
import com.example.petbutler.service.PetService;
import com.example.petbutler.service.UserService;
import java.security.Principal;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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

  private final JwtTokenUtils JwtTokenUtils;

  private final PetService petService;

  /**
   * 로그인 페이지 이동
   */
  @RequestMapping("/sign-in")
  public String getSignInPage(HttpServletRequest request) {

    // 현재 페이지 세션에 저장
    String referer = request.getHeader("Referer");

    if (referer != null && !referer.contains("/sign-in")) {
      request.getSession().setAttribute("prevPage", referer);
    }

    return "user/sign-in";

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

    // JWT 토큰 등록
    JwtTokenUtils.createToken(user);

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
   * (로그인 후) 펫 등록 페이지 이동
   */
  @GetMapping("/register-pet")
  public String getRegisterPetPage(){

    return "user/register-pet";

  }


  /**
   * (로그인 후) 반려동물 등록
   */
  @PostMapping("/register-pet")
  public String registerPets(PetRegisterForm petRegisterForm, Principal principal, Model model){

    boolean petRegisterResult = petService.registerPetsAfterSignUp(principal.getName(), petRegisterForm);

    if (petRegisterResult) {

      model.addAttribute("popupTitle", "반려동물 등록 완료 안내");
      model.addAttribute("popupMsg", "반려동물 등록이 정상적으로 완료되었습니다.");

    } else {

      model.addAttribute("popupTitle", "반려동물 등록 에러 안내");
      model.addAttribute("popupMsg", "반려동물 등록이 정상적으로 이루어지지 않았습니다.");

    }

    return "user/register-pet";

  }

  /**
   * 회원 조회
   */
  @GetMapping("/mypage")
  public String getCustomerDetail(Principal principal, Model model) {

    String email = principal.getName();

    UserSearch detail = userService.getUserDetailByEmail(email);

    model.addAttribute("detail", detail);

    return "user/mypage";
  }

  /**
   * 회원수정
   */
  @PutMapping(value = "/mypage", consumes = MediaType.APPLICATION_JSON_VALUE)
  public String updateUser(Principal principal, @RequestBody UserSearch detail){

    String email = principal.getName();

    userService.updateUser(email, detail);

    return "redirect:user/mypage";
  }

  /**
   * 회원탈퇴 페이지 이동
   *
   */
  @GetMapping("/withdraw")
  public String getDeletePage(Principal principal, Model model) {

    String email = principal.getName();

    UserSearch detail = userService.getUserDetailByEmail(email);

    model.addAttribute("detail", detail);

    return "user/mypage";
  }

  /**
   * 회원탈퇴
   * - 물리삭제 아닌 플래그 처리
   */
  @DeleteMapping("/withdraw")
  public String deleteCustomer(Principal principal){

    String email = principal.getName();

    userService.withdraw(email);

    return "redirect:/";
  }

}
