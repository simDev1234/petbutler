package com.example.petbutler.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.example.petbutler.dto.CustomerSignUpForm;
import com.example.petbutler.dto.PetDto;
import com.example.petbutler.entity.Customer;
import com.example.petbutler.exception.ButlerUserException;
import com.example.petbutler.exception.type.ErrorCode;
import com.example.petbutler.service.impl.CustomerServiceImpl;
import com.example.petbutler.service.impl.PetServiceImpl;
import com.example.petbutler.service.impl.SellerServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.FileInputStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

  @MockBean
  private CustomerServiceImpl customerService;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  @DisplayName("success: 로그인 페이지 이동")
  void testGetSignInPage() throws Exception {

    mockMvc.perform(get("/users/sign-in"))
        //.andExpect(status().isOk())
        .andExpect(view().name("users/sign-in"));

  }

  @Test
  @DisplayName("success: 회원가입 페이지 이동")
  void testGetSignUpPage() throws Exception {

    mockMvc.perform(get("/users/customer/sign-up"))
        .andExpect(status().isOk())
        .andExpect(view().name("users/customer/sign-up"))
        .andDo(print());

  }

  @Test
  @DisplayName("success: 이메일 회원가입")
  void testRegisterUserByEmail() throws Exception {

    // given
    CustomerSignUpForm customerSignUpForm =
        CustomerSignUpForm.builder()
            .email("test@gmail.com")
            .password(BCrypt.hashpw("password", BCrypt.gensalt()))
            .butlerLevel(1)
            .build();

    PetDto[] petDtos = new PetDto[]{
        new PetDto("cat", "bomi"),
        new PetDto("cat", "mama")
    };

    MockMultipartFile[] files = new MockMultipartFile[]{
        new MockMultipartFile("file", new FileInputStream("/files/test/test-image1.jpg")),
        new MockMultipartFile("file", new FileInputStream("/files/test/test-image2.jpg"))
    };

    Customer customer = Customer.builder()
        .email("test@gmail.com")
        .password(BCrypt.hashpw("password", BCrypt.gensalt()))
        .butlerLevel(1)
        .build();

    given(customerService.signUpByEmail(customerSignUpForm, petDtos, files))
        .willReturn(customer);

    // when & then
    mockMvc.perform(
            post("/users/customer/sign-up")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .content(objectMapper.writeValueAsString(customerSignUpForm))
                .content(objectMapper.writeValueAsString(petDtos))
                .content(objectMapper.writeValueAsString(files))
        )
        .andExpect(status().isOk())
        .andExpect(model().attributeExists("email"))
        .andExpect(view().name("users/sign-up-complete"));

  }

  @Test
  @DisplayName("success: 로그인 페이지 이동")
  void emailAuth() throws Exception {

    // when & then
    mockMvc.perform(
            get("/users/email-auth?authKey=aslkdfaposifpwjqasd")
        )
        .andExpect(status().isOk())
        .andExpect(view().name("users/register-pets"));

  }

  @Test
  void registerPets() {
  }
}