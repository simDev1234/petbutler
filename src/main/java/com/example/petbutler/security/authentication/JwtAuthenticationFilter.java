package com.example.petbutler.security.authentication;

import java.io.IOException;
import java.util.Objects;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtTokenProvider jwtTokenProvider;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                  FilterChain filterChain) throws ServletException, IOException {

    // 요청 헤더에서 jwt 토큰을 가져온다.
    String token = jwtTokenProvider.resolveTokenFromRequest(request);

    // 토큰 유효성 파악
    if (Objects.nonNull(token) && jwtTokenProvider.validateToken(token)) {
      // 유효할 경우 유저 정보를 받아온다.
      Authentication authentication = jwtTokenProvider.getAuthentication(token);
      // SecurityContext에 Authentication 저장
      SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    filterChain.doFilter(request, response);
  }

}
