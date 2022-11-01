package com.example.petbutler.config;

import com.example.petbutler.repository.SellerRepository;
import com.example.petbutler.service.impl.SellerServiceImpl;
import com.example.petbutler.utils.EmailSendUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SellerSecurityConfig extends WebSecurityConfigurerAdapter {

  private final SellerRepository sellerRepository;

  private final EmailSendUtils emailSendUtils;

  /**
   * Http Security
   */
  @Override
  protected void configure(HttpSecurity http) throws Exception {

    // 임시
    http.csrf().disable();

    http.authorizeRequests()
        .antMatchers(
            "/",
            "/users/sign-in",
            "/users/customer/sign-up/",
            "/users/seller/sign-up/",
            "/users/email-auth"
        )
        .permitAll();

    http.formLogin()
        .loginPage("/users/sign-in")
        .defaultSuccessUrl("/")
        .failureUrl("/users/sign-in/fail")
        .permitAll();

    http.logout()
        .logoutRequestMatcher(new AntPathRequestMatcher("/users/logout"))
        .logoutSuccessUrl("/")
        .invalidateHttpSession(true);

  }

  /**
   * Authentication (인증)
   */
  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {

    auth.userDetailsService(new SellerServiceImpl(sellerRepository, emailSendUtils))
        .passwordEncoder(PasswordEncoderFactories.createDelegatingPasswordEncoder());

    super.configure(auth);
  }

  /**
   * Web Recourse Security
   */
  @Override
  public void configure(WebSecurity web) throws Exception {

    web.ignoring()
        .antMatchers("/","/*.html","/**/*.html", "/*.png", "/**/*.png", "/*.jpg",
            "/**/*.jpg","/**/*.css", "/*.js", "/**/*.js");

    super.configure(web);
  }

}
