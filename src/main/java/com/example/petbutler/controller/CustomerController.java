package com.example.petbutler.controller;

import com.example.petbutler.dto.CustomerSignUpForm;
import com.example.petbutler.dto.PetRegisterForm;
import com.example.petbutler.entity.Customer;
import com.example.petbutler.service.CustomerService;
import com.example.petbutler.service.PetService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
  @PostMapping(value = "/users/customer/sign-up", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public String signUpByEmail(CustomerSignUpForm customerSignUpForm,
                              PetRegisterForm petRegisterForm, Model model) {

    // register user
    Customer customer = customerService.signUpByEmail(customerSignUpForm);

    try {

      // register pets
      petService.registerPetsWhenSignUp(customer, petRegisterForm);

      // if success -> send auth email with "sign-in" page mapping
      customerService.sendEmailToUser(customer.getEmail(), customer.getEmailAuthKey(), "/users/sign-in");

    } catch(IOException e) {

      // if fail    -> send auth email with "register-pet" page mapping
      customerService.sendEmailToUser(customer.getEmail(), customer.getEmailAuthKey(), "/users/customer/register-pet");

    }

    model.addAttribute("email", customer.getEmail());

    return "users/sign-up-complete";

  }

  /**
   * 이메일 인증
   */
  @GetMapping("/users/customer/email-auth")
  public String authorizeUserByEmail(@RequestParam(name = "authKey") String emailAuthKey,
                                     @RequestParam(name = "mapping-path") String mappingPath) {

    customerService.emailAuth(emailAuthKey);

    return mappingPath;

  }


}
