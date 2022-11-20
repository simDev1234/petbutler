## 1. Post를 통해 동일 parameter명의 Multi-part 데이터를 가져오는 과정에서 NullPointerException 발생
    - 원인  : UserRegisterForm안에 PetRegisterForm을 넣고 해당 Object안에서 String[] name과 같이 파라미터를 읽어 매핑하려 했다. --> 매핑 불가
    - 해결  : UserRegisterForm와 PetRegisterForm을 별도로 받도록 했다.
    - 참고  :
      스프링 MVC에서는 URI의 query parameter 뿐만 아니라, form data, multipart request 모두 @RequestParam으로 처리 가능하다.
      이유는 서블릿 API가 query parameter, form data, multi-part data를 "parameters"로 묶어 처리하기 때문이며,
      데이터에 대한 파싱 처리까지 그 api에서 처리하기 때문이다.
      좀 더 깔끔한 처리를 하기 위해서는 다수의 @RequestParam을 Object 안으로 묶어 받을 수 있다. 
    - rf. formData 관련 : https://inpa.tistory.com/entry/JS-%F0%9F%93%9A-FormData-%EC%A0%95%EB%A6%AC-fetch-api

```java

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PetRegisterForm {

  private MultipartFile[] thumbnail;

  private String[] kind;

  private String[] name;

  public List<Pet> toPets(User user, List<FilePath> newFilePaths) {

    List<Pet> pets = new ArrayList();

    for (int i = 0; i < this.name.length; i++) {

      pets.add(Pet.builder()
                  .user(user)
                  .kind(this.kind[i])
                  .name(this.kind[i])
                  .thumbnailLocalPath(newFilePaths.get(i).getLocalPath())
                  .thumbnailUrlPath(newFilePaths.get(i).getUrlPath())
                  .build());

    }

    return pets;
  }


}

```
<br>

## 2. 파일 경로를 인식하지 못함
```shell
java.nio.file.InvalidPathException:
    Illegal char <:> at index 4:
      http:\127.0.0.1:8080\http:\127.0.0.1:8080\2022\10\30\4f2ae710006c486a8682eb8763c3ee1a.jpg
```
    - 원인  : 'http:\127.0.0.1:8080' 가 중복되어 입력되었다.
    - 해결  : 중복되지 않도록 수정했다.
    - 추가  : application.yml에 호스트 주소를 넣고 가져올 수 있으나, 서버와 관련하여 여러 정보를 정리하여 보기 위해 Config클래스를 만들었다.
```java
/* 이메일 인증 시 서버 주소 필요하여 별도로 서버 설정 */
/* rf. https://www.baeldung.com/spring-boot-configuration-metadata */
@Configuration
@ConfigurationProperties(prefix = "server")
@Getter
@Setter
public class ServerPropertyConfig {

  private String ip = "127.0.0.1";

  private int port = 8080;

  private String address = String.format("http://%s:%d", ip, port);

  private String petThumbnailLocalRoot = "C:\\sebinSample\\petbutler\\src\\main\\resources\\static\\files";

  private String petThumbnailUrlRoot = "/files";

}

```
<br>

## 3. 트랜잭션 롤백 현상
```shell
Caused by: org.springframework.transaction.UnexpectedRollbackException:
Transaction silently rolled back because it has been marked as rollback-only
```
    - 원인 : 자식 트랜잭션에서 한 번 롤백된 트랜잭션은 rollback-only로 간주되어 try-catch에서 잡히지 않는다.
    - 대안 : Propagartion을 Nested로 바꾼다 --> Nested 관련 오류 발생 *save point에 대한 에러를 만들어낼 가능성이 있다.
    - 해결 : 하나의 서비스에서 다른 서비스의 메소드를 부르는 부모-자식 관계를 형성하지 않고, Controller에서 각각 메소드를 불렀다. 
    rf. https://techblog.woowahan.com/2606/
```java
class UserController{
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
}
```
<br>

## 4. Spring Security - NoSuchBeanDefinitionException
    - 원인   : 회원을 Customer와 Seller로 구분한 상태에서, Spring Security를 각각 설정하였고, 테스트를 진행하였는데 위 이슈가 발생하였다. 원인은 @WebMvcTest를 진행할 경우 스프링 시큐러티 환경을 읽지 못하기 때문에 발생한 이슈였다. @WebMvcTest는 @Controller/@RestController, @Service, @Respository만 읽을 수 있다. 
    - 해결 : @AutoConfigureMockMvc를 통해 해결했다.


<br>

## 5. 로그인 시, AuthenticationProvider가 없다는 메세지 발생
```shell
org.springframework.security.authentication.ProviderNotFoundException: 
No AuthenticationProvider found for org.springframework.security.authentication.UsernamePasswordAuthenticationToken
```
<img src="https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fk.kakaocdn.net%2Fdn%2FbeDENY%2FbtrBs0cquNc%2FPkwRQzgyzhoy1ecQrlQOJk%2Fimg.png">

<img src="https://velog.velcdn.com/images/dailylifecoding/post/5464e017-a5be-487e-bb24-e10216c803a8/image.png">


    - 원인 : (파악중) 
    - 출처 : https://dev-coco.tistory.com/174
             https://velog.io/@dailylifecoding/spring-security-authentication-token
    - 스프링 시큐러티에서는 인증정보를 하나의 토큰 개념으로 관리한다.
    - 토큰은 인증 전과 인증 후에 내부 데이터가 다르다.
      * 인증 전 : id, password
      * 인증 후 : User 객체, 권한 정보
      - 인증 후 토큰은 SecurityContext에 저장되어 전역적으로 참조할 수 있다.
    - 토큰의 구조
      Principal(사용자 아이디 또는 User객체), credentials(비밀번호),
      authorities(인증 사용자의 권한 목록), details(인증부가정보), authenticated(인증 여부)
    - 스프링 시큐러티 Authentication Architecture 절차
    (1) 사용자가 로그인 정보를 입력한 후 요청을 보낸다(Http Request)
    (2) AuthenticationFilter가 요청을 가로채어 UsernamePasswordAuthenticationToken의 인증용 객체를 생성한다.
    (3) AuthenticationManager의 구현체인 AuthenticationProvider에게 UsernamePasswordToken을 전달한다.
    (4) AuthenticationManager는 등록된 AuthenticationProvider들을 조회하여 인증을 요구한다.
    (5) 실제 DB에서 사용자 인증정보를 가져오는 UserDetailsService에 사용자 정보를 넘겨준다.
    (6) 넘겨받은 사용자 정보를 통해 DB에서 찾은 사용자 정보인 UserDetails 객체를 만든다.
    (7) AuthenticationProvider들은 UserDetails를 넘겨받고 사용자 정보를 비교한다.
    (8) 인증이 완료되면 권한 등의 사용자 정보를 담은 Authentication 객체를 반환한다.
    (9) 다시 최초의 AuthenticationFilter에 Authentication 객체가 반환된다.
    (10) Authentication 객체를 SecurityContext에 저장한다.

<br>

## 6. Access to DialectResolutionInfo cannot be null when 'hibernate.dialect' not set
    - 원인 : @Query 작성중 위 오류가 나타났으며 원인은 jpa에서 사용하는 database vendor를 명시하지 않아 sql문법을 찾을 수 없기 때문이었다.
    - 해결 : applicaiton.yml에 spring.jpa.database=mysql 명시

<br>

## 7. java.lang.IllegalArgumentException: An invalid character [32] was present in the Cookie value
    - 원인 : Cookie에 Invalid Character [32] 가 있다. 
      >> Tomcat 8.5 부터 추가된 기본 쿠키 규칙 때문이다.
      >> ';', ',', '=', ' '은 쿠키 값으로 이용될 수 없는데 'Bearer dskfjasdfjpasdofi(토큰값)'-> 공백을 넣었기 때문에 발생
    - 해결 : Bearer 다음에 공백을 제거하고 : 을 추가
    - 참조 : https://shanepark.tistory.com/133

## 8. csrf 관련 에러 : 쿠키 설정 후 접속이 계속 되지 않는 이슈
    - 원인 : 스프링 시큐러티는 디폴트로 헤더에 Cache-Control을 추가하고, no-cache, no-store 등의 옵션을 추가한다. 이에 따라 쿠키 사용 또한 제한된다.
    - 해결 : csrf.disabled() 를 하면 csrf 토큰을 만들지 않고, Cache-Control을 해제한다. 이렇게 되면 쿠키를 사용할 수는 있으나 csrf에 취약해지는 단점이 생긴다. (공격자가 http url을 알면 공격가능)
    - 대안 : Access token은 js에 저장하고, Refresh Token은 쿠키에 저장하는 방법
    - 참조 : https://developer-ping9.tistory.com/234
            https://developer-ping9.tistory.com/234