package com.example.petbutler.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.example.petbutler.model.UserSignUpForm;
import com.example.petbutler.model.PetRegisterForm;
import com.example.petbutler.persist.entity.User;
import com.example.petbutler.persist.entity.Pet;
import com.example.petbutler.service.PetService;
import com.example.petbutler.service.impl.UserServiceImpl;
import com.example.petbutler.model.constants.UserRole;
import java.io.FileInputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

  @MockBean
  private UserServiceImpl UserService;

  @MockBean
  private PetService petService;

  @Autowired
  private MockMvc mockMvc;

  @Test
  @DisplayName("success: 로그인 페이지 이동")
  void testGetSignInPage() throws Exception {

    mockMvc.perform(get("/users/sign-in"))
        .andExpect(status().isOk())
        .andExpect(view().name("user/sign-in"));

  }

  @Test
  @DisplayName("success: 회원가입 페이지 이동")
  void testGetSignUpPage() throws Exception {

    mockMvc.perform(get("/users/sign-up"))
        .andExpect(status().isOk())
        .andExpect(view().name("user/sign-up"))
        .andDo(print());

  }

  @Test
  @DisplayName("success: 이메일 회원가입")
  void testSignUpByEmail() throws Exception {

    // given
    UserSignUpForm userSignUpForm =
        UserSignUpForm.builder()
            .email("test@gmail.com")
            .password(BCrypt.hashpw("password", BCrypt.gensalt()))
            .butlerLevel(1)
            .build();

    MockMultipartFile[] files = new MockMultipartFile[]{
        new MockMultipartFile("file1", new FileInputStream("C:\\sebinSample\\petbutler\\src\\main\\resources\\static\\files\\test\\test-image1.jpg")),
        new MockMultipartFile("file2", new FileInputStream("C:\\sebinSample\\petbutler\\src\\main\\resources\\static\\files\\test\\test-image2.jpg"))
    };

    PetRegisterForm petRegisterForm = PetRegisterForm.builder()
        .thumbnail(files)
        .kind(new String[]{"cat", "cat"})
        .name(new String[]{"pet1", "pet2"})
        .build();

    User user = User.builder()
        .id(1L)
        .userRole(UserRole.ROLE_REGULAR)
        .email("test@gmail.com")
        .password(BCrypt.hashpw("password", BCrypt.gensalt()))
        .emailAuthKey("authkey")
        .emailAuthYn(false)
        .emailAuthExpiredAt(LocalDateTime.now().plusDays(1))
        .butlerLevel(1)
        .build();

    given(UserService.signUpByEmail(any(), any()))
        .willReturn(user.getEmail());

    // when & then
    mockMvc.perform(
            multipart("/users/sign-up")
                .file(files[0]).file(files[1])
                .param("email", "test@gmail.com")
                .param("password", "password")
                .param("butlerLevel", "1")
                .param("kind", "cat")
                .param("kind", "cat")
                .param("name", "pet1")
                .param("name", "pet2")
        )
        .andExpect(status().isOk())
        .andExpect(model().attributeExists("email"))
        .andExpect(view().name("users/sign-up-complete"));

  }

  @Test
  @DisplayName("success: 이메일 인증")
  void testAuthorizeUserByEmail() throws Exception {

    // given
    UserService.emailAuth(anyString(), anyString());

    String mappingPath = "user/register-pet";

    // when & then
    mockMvc.perform(
            get("/users/email-auth?auth-key=key&mapping-path=" + mappingPath)
        )
        .andExpect(status().isOk())
        .andExpect(view().name(mappingPath));

  }

  @Test
  @DisplayName("success: 로그인 후 반려동물 등록")
  void registerPets() throws Exception {

    // given
    String userName = "test@gmail.com";

    User user = User.builder()
        .id(1L)
        .userRole(UserRole.ROLE_REGULAR)
        .email("test@gmail.com")
        .password(BCrypt.hashpw("password", BCrypt.gensalt()))
        .emailAuthKey("authkey")
        .emailAuthYn(false)
        .emailAuthExpiredAt(LocalDateTime.now().plusDays(1))
        .butlerLevel(1)
        .build();

    MockMultipartFile[] files = new MockMultipartFile[]{
        new MockMultipartFile("file1", new FileInputStream("C:\\sebinSample\\petbutler\\src\\main\\resources\\static\\files\\test\\test-image1.jpg")),
        new MockMultipartFile("file2", new FileInputStream("C:\\sebinSample\\petbutler\\src\\main\\resources\\static\\files\\test\\test-image2.jpg"))
    };

    List<Pet> pets = new ArrayList<>(
        Arrays.asList(
            new Pet(1L, user, "C:/localpath", "http://localhost:8080/files/extra", "cat", "cat1"),
            new Pet(2L, user, "C:/localpath", "http://localhost:8080/files/extra", "cat", "cat2")
        )
    );

    given(petService.registerPetsAfterSignUp(anyString(), any()))
            .willReturn(true);

    // when & then
    mockMvc.perform(
        multipart("/users/register-pet")
            .file(files[0]).file(files[1])
            .param("kind", "cat")
            .param("kind", "cat")
            .param("name", "pet1")
            .param("name", "pet2"))
        .andExpect(status().isOk())
        .andExpect(model().attribute("popupTitle", "반려동물 등록 완료 안내"))
        .andExpect(model().attribute("popupMsg", "반려동물 등록이 정상적으로 완료되었습니다."))
        .andExpect(view().name("user/register-pet"));

  }
}