package com.example.petbutler.security;

import com.example.petbutler.security.authentication.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private final JwtAuthenticationFilter jwtAuthenticationFilter;

  /**
   * Http Security
   */
  @Override
  protected void configure(HttpSecurity http) throws Exception {

    // 임시
    http.csrf().disable();

    http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    http.authorizeRequests()
        .antMatchers(
            "/",
            "/user/sign-in",
            "/user/sign-up",
            "/user/email-auth",
            "/user/email-auth/**"
        )
        .permitAll();

    http.exceptionHandling()
        .accessDeniedPage("/error/access-denied");

    http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

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

  @Override
  @Bean
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }
}
