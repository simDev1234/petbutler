package com.example.petbutler.config;

import com.example.petbutler.repository.CustomerRepository;
import com.example.petbutler.service.CustomerService;
import com.example.petbutler.type.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CustomerSecurityConfig extends WebSecurityConfigurerAdapter {

  private final CustomerService customerService;

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

    http.authorizeRequests()
        .antMatchers("/admin/**")
        .hasAuthority(UserRole.ROLE_CUSTOMER.name());

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

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(customerService)
        .passwordEncoder(PasswordEncoderFactories.createDelegatingPasswordEncoder());

    super.configure(auth);
  }
}
