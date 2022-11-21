package com.example.petbutler.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.example.petbutler.model.UserDetailForm;
import com.example.petbutler.model.UserSignUpForm;
import com.example.petbutler.model.PetRegisterForm;
import com.example.petbutler.persist.entity.User;
import com.example.petbutler.persist.entity.Pet;
import com.example.petbutler.exception.ButlerUserException;
import com.example.petbutler.exception.constants.ErrorCode;
import com.example.petbutler.persist.UserRepository;
import com.example.petbutler.service.PetService;
import com.example.petbutler.model.constants.UserRole;
import com.example.petbutler.model.constants.UserStatus;
import com.example.petbutler.utils.EmailSendUtils;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.bcrypt.BCrypt;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private EmailSendUtils emailSendUtils;

  @Mock
  private PetService petService;

  @InjectMocks
  private UserServiceImpl UserService;

  @Test
  @DisplayName("fail: 회원가입 - 이미 가입한 회원인 경우")
  void testSignUpByEmail_customerAlreadyExist() throws IOException {

    //given
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

    List<Pet> pets = new ArrayList<>(
        Arrays.asList(
            new Pet(1L, user, "C:/localpath", "http://localhost:8080/files/extra", "cat", "cat1"),
            new Pet(2L, user, "C:/localpath", "http://localhost:8080/files/extra", "cat", "cat2")
        )
    );

    given(userRepository.findByEmail(anyString()))
        .willReturn(Optional.of(user));

    //when
    ButlerUserException exception = assertThrows(ButlerUserException.class,
        () -> UserService.signUpByEmail(userSignUpForm, petRegisterForm));

    //then
    assertEquals(ErrorCode.USER_ALREADY_EXIST, exception.getErrorCode());

  }

  @Test
  @DisplayName("success: 회원가입")
  void testSignUpByEmailSuccess() throws IOException {

    //given
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

    List<Pet> pets = new ArrayList<>(
        Arrays.asList(
            new Pet(1L, user, "C:/localpath", "http://localhost:8080/files/extra", "cat", "cat1"),
            new Pet(2L, user, "C:/localpath", "http://localhost:8080/files/extra", "cat", "cat2")
        )
    );

    given(userRepository.findByEmail(anyString()))
        .willReturn(Optional.empty());

    given(userRepository.save(any()))
        .willReturn(User.toEntity(userSignUpForm));

    given(petService.registerPetsWhenSignUp(any(), any()))
        .willReturn(pets);

    ArgumentCaptor<User> customerCaptor = ArgumentCaptor.forClass(User.class);

    //when
    String email = UserService.signUpByEmail(userSignUpForm, petRegisterForm);

    //then
    verify(userRepository, times(1)).save(customerCaptor.capture());
    assertEquals("test@gmail.com" , email);
    assertEquals(1, customerCaptor.getValue().getButlerLevel());

  }

  @Test
  @DisplayName("fail: 이메일 전송 - 회원이 존재하지 않는 경우")
  void testEmailAuth_customerNotFound() {

    //given
    String email = "test@gmail.com";

    String emailAuthKey = UUID.randomUUID().toString().replace("-","");

    given(userRepository.findByEmailAndAndEmailAuthKey(anyString(), anyString()))
        .willReturn(Optional.empty());

    // when
    ButlerUserException exception = assertThrows(ButlerUserException.class,
        () -> UserService.emailAuth(email, emailAuthKey));

    // then
    assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());

  }

  @Test
  @DisplayName("fail: 이메일 전송 - 이미 아이디가 활성화된 경우")
  void testEmailAuth_customerAlreadyAuthorized() {

    //given
    String email = "test@gmail.com";

    String emailAuthKey = UUID.randomUUID().toString().replace("-","");

    User user = User.builder()
        .id(1L)
        .userRole(UserRole.ROLE_REGULAR)
        .email("test@gmail.com")
        .password(BCrypt.hashpw("password", BCrypt.gensalt()))
        .butlerLevel(1)
        .phone("010-1234-5678")
        .userStatus(UserStatus.IN_USE)
        .emailAuthYn(true)
        .build();

    given(userRepository.findByEmailAndAndEmailAuthKey(anyString(), anyString()))
        .willReturn(Optional.of(user));

    // when
    ButlerUserException exception = assertThrows(ButlerUserException.class,
        () -> UserService.emailAuth(email, emailAuthKey));

    // then
    assertEquals(ErrorCode.USER_ALREADY_AUTHORIZED, exception.getErrorCode());

  }

  @Test
  @DisplayName("fail: 이메일 전송 - 정지 상태인 경우")
  void testEmailAuth_customerStatusStop(){

  }

  @Test
  @DisplayName("fail: 이메일 전송 - 탈퇴 상태인 경우")
  void testEmailAuth_customerStatusWithdraw(){

  }

  @Test
  @DisplayName("fail: 이메일 전송 - 인증 토큰 만료")
  void testEmailAuth_emailAuthTokenExpired(){

  }

  @Test
  @DisplayName("fail: 회원 조회 - 회원이 존재하지 않는 경우")
  void testGetCustomerDetailByEmail_customerNotFound() {

    //given
    String email = "test@gmail.com";

    given(userRepository.findByEmail(anyString()))
        .willReturn(Optional.empty());

    // when
    ButlerUserException exception = assertThrows(ButlerUserException.class,
        () -> UserService.getUserDetailByEmail(email));

    // then
    assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());

  }

  @Test
  @DisplayName("success: 회원 조회")
  void testGetCustomerDetailByEmail() {

    //given
    String email = "test@gmail.com";

    User user = User.builder()
        .id(1L)
        .userRole(UserRole.ROLE_REGULAR)
        .email("test@gmail.com")
        .password(BCrypt.hashpw("password", BCrypt.gensalt()))
        .butlerLevel(1)
        .phone("010-1234-5678")
        .build();

    given(userRepository.findByEmail(anyString()))
        .willReturn(Optional.of(user));

    // when
    UserDetailForm detail = UserService.getUserDetailByEmail(email);

    // then
    assertEquals("test@gmail.com", detail.getEmail());
    assertEquals(1, detail.getButlerLevel());
    assertEquals("010-1234-5678", detail.getPhone());

  }

  @Test
  @DisplayName("fail: 회원 수정 - 회원이 존재하지 않는 경우")
  public void testUpdateCustomerDetail_customerNotFound(){

    //given
    String email = "test@gmail.com";

    UserDetailForm detail = UserDetailForm.builder()
                                          .butlerLevel(2)
                                          .phone("010-5678-9876")
                                          .build();

    given(userRepository.findByEmail(anyString()))
        .willReturn(Optional.empty());

    // when
    ButlerUserException exception = assertThrows(ButlerUserException.class,
        () -> UserService.updateUser(email, detail));

    // then
    assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());

  }

  @Test
  @DisplayName("success: 회원 수정")
  public void testUpdateCustomerDetail(){

    //given
    String email = "test@gmail.com";

    UserDetailForm detail = UserDetailForm.builder()
                                          .butlerLevel(2)
                                          .phone("010-5678-9876")
                                          .build();

    User user = User.builder()
        .id(1L)
        .userRole(UserRole.ROLE_REGULAR)
        .email("test@gmail.com")
        .password(BCrypt.hashpw("password", BCrypt.gensalt()))
        .butlerLevel(1)
        .phone("010-1234-5678")
        .build();

    given(userRepository.findByEmail(anyString()))
        .willReturn(Optional.of(user));

    // when
    UserService.updateUser(email, detail);

    // then

  }

  @Test
  @DisplayName("fail: 회원 탈퇴 - 회원이 존재하지 않는 경우")
  public void testDeleteCustomer_customerNotFound(){

    //given
    String email = "test@gmail.com";

    given(userRepository.findByEmail(anyString()))
        .willReturn(Optional.empty());

    // when
    ButlerUserException exception = assertThrows(ButlerUserException.class,
        () -> UserService.withdraw(email));

    // then
    assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());

  }

  @Test
  @DisplayName("success: 회원 탈퇴")
  public void testDeleteCustomer(){

    //given
    String email = "test@gmail.com";

    User user = User.builder()
        .id(1L)
        .userRole(UserRole.ROLE_REGULAR)
        .email("test@gmail.com")
        .password(BCrypt.hashpw("password", BCrypt.gensalt()))
        .butlerLevel(1)
        .phone("010-1234-5678")
        .userStatus(UserStatus.IN_USE)
        .build();

    given(userRepository.findByEmail(anyString()))
        .willReturn(Optional.of(user));

    //when
    UserService.withdraw(email);

    //then

  }


}