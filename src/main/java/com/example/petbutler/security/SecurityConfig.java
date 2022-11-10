package com.example.petbutler.security;

import com.example.petbutler.security.authentication.JwtAuthenticationFilter;
import com.example.petbutler.security.authentication.JwtTokenUtils;
import com.example.petbutler.service.UserService;
import com.example.petbutler.type.UserRole;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private final UserService UserService;

  private final JwtTokenUtils jwtTokenUtils;

  @Bean
  public static PasswordEncoder getPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }

  /**
   * Http Security
   */
  @Override
  protected void configure(HttpSecurity http) throws Exception {

    // 임시
    http.csrf().disable();
//        .sessionManagement()
//        .sessionCreationPolicy(SessionCreationPolicy.STATELESS); // 세션-쿠키 방식x
//
//    http.addFilterBefore(new JwtAuthenticationFilter(jwtTokenUtils), UsernamePasswordAuthenticationFilter.class);

    http.authorizeRequests()
        .antMatchers(
            "/user/sign-in",
            "/users/sign-up",
            "/users/email-auth",
            "/users/email-auth/**"
        )
        .permitAll();

    http.authorizeRequests()
        .antMatchers("/admin/*", "/admin/**")
        .hasAuthority(UserRole.ROLE_ADMIN.name());

    http.formLogin()
        .loginPage("/user/sign-in")
        .usernameParameter("username")
        .passwordParameter("password")
        .defaultSuccessUrl("/")
        .loginProcessingUrl("/user/sign-in")
        .successHandler((request, response, authentication)
            -> sendRedirectToPrevPage(request, response))
        .failureHandler((request, response, exception)
            -> response.sendRedirect("/user/sign-in"))
        .permitAll();

    http.logout()
        .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
        .logoutSuccessUrl("/")
        .invalidateHttpSession(true);

    http.exceptionHandling()
        .accessDeniedPage("/error/access-denied");

  }

  private static void sendRedirectToPrevPage(HttpServletRequest request, HttpServletResponse response)
      throws IOException {

    HttpSession session = request.getSession();

    if (session != null) {
      String redirectUrl = (String) session.getAttribute("prevPage");

      if (redirectUrl != null) {
        session.removeAttribute("prevPage");
        response.sendRedirect(redirectUrl);
      } else {
        response.sendRedirect("/");
      }
    } else {
      response.sendRedirect("/");
    }

  }

  /**
   * Web Security
   */
  @Override
  public void configure(WebSecurity web) throws Exception {

    web.ignoring()
        .antMatchers("/", "/*.html", "/**/*.html", "/*.png", "/**/*.png", "/*.jpg",
            "/**/*.jpg", "/**/*.css", "/*.js", "/**/*.js");

    super.configure(web);
  }

  /**
   * Authentication
   */
  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {

    auth.userDetailsService(UserService).passwordEncoder(getPasswordEncoder());

    super.configure(auth);
  }

}
