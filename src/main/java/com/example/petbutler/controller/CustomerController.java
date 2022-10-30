package com.example.petbutler.controller;

import com.example.petbutler.dto.CustomerSignUpForm;
import com.example.petbutler.dto.PetDto;
import com.example.petbutler.entity.Customer;
import com.example.petbutler.service.CustomerService;
import com.example.petbutler.service.PetService;
import java.io.IOException;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
public class CustomerController {

  private final CustomerService customerService;

  private final PetService petService;


  /**
   * 회원가입 페이지 이동
   */
  @GetMapping("/users/customer/sign-up")
  public String getCustomerSignUpPage() {

    return "users/customer/sign-up";

  }

  /**
   * 회원 가입
   */
  @PostMapping(value = "/users/customer/sign-up")
  public String signUpByEmail(CustomerSignUpForm form,
                              @RequestParam("thumbnail") MultipartFile[] files,
                              @RequestParam("kind") String[] kinds,
                              @RequestParam("name") String[] names,
                              Model model) {

    try {

      // sign up
      Customer customer = customerService.signUpByEmail(form);

      // register pets
      if (Objects.nonNull(names) && names.length > 0) {
        petService.registerPetsWhenSignUp(customer, PetDto.of(kinds, names), files);
      }

      model.addAttribute("email", customer.getEmail());

    } catch (IOException e) {

      model.addAttribute("error", e.getMessage());

    }

    return "users/sign-up-complete";

  }



  /**
   * 이메일 인증
   */
  @GetMapping("/users/customer/email-auth")
  public String authorizeUserByEmail(@RequestParam(name = "authKey") String emailAuthKey) {

    customerService.emailAuth(emailAuthKey);

    return "users/customer/register-pets";

  }


}
