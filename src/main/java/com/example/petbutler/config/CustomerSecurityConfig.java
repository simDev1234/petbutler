package com.example.petbutler.config;

import com.example.petbutler.repository.CustomerRepository;
import com.example.petbutler.service.CustomerService;
import com.example.petbutler.service.PetService;
import com.example.petbutler.service.impl.CustomerServiceImpl;
import com.example.petbutler.type.UserRole;
import com.example.petbutler.utils.EmailSendUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
public class CustomerSecurityConfig extends WebSecurityConfigurerAdapter {

  private final CustomerRepository customerRepository;

  private final EmailSendUtils emailSendUtils;

  private final PetService petService;

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(new CustomerServiceImpl(customerRepository, emailSendUtils, petService))
        .passwordEncoder(PasswordEncoderFactories.createDelegatingPasswordEncoder());

    super.configure(auth);
  }

}
