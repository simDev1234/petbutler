package com.example.petbutler.security.authentication;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.GenericFilter;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends GenericFilter {

  private final JwtTokenUtils jwtTokenUtils;


  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    // http header에서 token 추출
    String token = jwtTokenUtils.getToken((HttpServletRequest) request);

    System.out.println("token : " + token);

    try {
      // 토큰이 존재하고 유효할 경우
      if (token != null && jwtTokenUtils.isValid(token)) {

        // 인증 정보 추출
        Authentication authentication = jwtTokenUtils.getAuthentication(token);

        // 시큐러티 컨텍스트에 인증 정보 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);

        System.out.println("인증 정보 추출 : " + jwtTokenUtils.getAuthentication(token).toString());
        System.out.println("시큐러티 컨텍스트에 인증 정보 저장");

      }
    } catch(Exception e) {

      log.error("Error occured : {}", e.getStackTrace());

    }

    chain.doFilter(request, response);

  }
}
