package com.example.petbutler.security;

import com.example.petbutler.security.authentication.JwtTokenUtils;
import com.example.petbutler.service.UserService;
import com.example.petbutler.type.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private final UserService UserService;

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
    http
        .csrf().disable();

    http.authorizeRequests()
        .antMatchers(
            "/",
            "/user/sign-in",
            "/user/sign-up",
            "/user/email-auth",
            "/user/email-auth/**"
        )
        .permitAll();

    http.authorizeRequests()
        .antMatchers("/admin/*", "/admin/**")
        .hasAuthority(UserRole.ROLE_ADMIN.name());

    http.formLogin()
        .loginPage("/user/sign-in")
        .usernameParameter("email")
        .passwordParameter("password")
        .failureHandler((request, response, exception)
            -> response.sendRedirect("/user/sign-in/fail"))
        .permitAll();

    http.logout()
        .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
        .logoutSuccessUrl("/")
        .invalidateHttpSession(true);

    http.exceptionHandling()
        .accessDeniedPage("/error/access-denied");

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
