package com.example.petbutler.config;

import com.example.petbutler.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Bean
  PasswordEncoder getPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }

  private final UserService userService;

  @Override
  protected void configure(HttpSecurity http) throws Exception {

    // 임시
    http.csrf().disable();

    http.authorizeRequests()
        .antMatchers(
            "/",
            "/users/sign-up",
            "/users/email-auth"
        )
        .permitAll();

    http.formLogin()
        .loginPage("/users/sign-in")
        .defaultSuccessUrl("/")
        .failureUrl("/users/sign-in/fail")
        .permitAll();

    http.authorizeRequests()
        .antMatchers("/admin/**")
        .hasAuthority("ROLE_ADMIN");

    super.configure(http);
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {

    auth.userDetailsService(userService)
        .passwordEncoder(getPasswordEncoder());

    super.configure(auth);
  }

}
