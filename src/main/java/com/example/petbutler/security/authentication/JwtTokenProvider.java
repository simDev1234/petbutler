package com.example.petbutler.security.authentication;

import com.example.petbutler.service.UserService;
import com.mysql.cj.util.StringUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

  @Value("{spring.jwt.secret}")
  public String secretKey;

  public static final String TOKEN_HEADER = "Authorization";

  public static final String TOKEN_PREFIX = "Bearer:";

  public final static String KEY_ROLES = "roles";
  
  public final static long TOKEN_EXPIRATION_TIME = 1000 * 60 * 60 * 5; // 5시간
  
  private final UserService userService;

  /**
   * 토큰 생성
   */
  public String generateToken(String email, List<String> roles){

    Date now = new Date();
    Date expiredDate = new Date(now.getTime() + TOKEN_EXPIRATION_TIME);

    Claims claims = Jwts.claims().setSubject(email);
    claims.put(KEY_ROLES, roles);

    return Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(now)
        .setExpiration(expiredDate)
        .signWith(SignatureAlgorithm.HS512, secretKey)
        .compact();
  }

  //TODO : refresh token 별도 생성 필요

  /**
   * 토큰을 통해 Authentication 추출
   */
  public Authentication getAuthentication(String token) {

    UserDetails userDetails = userService.loadUserByUsername(getEmail(token));

    return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());

  }

  /**
   * 토큰의 Claim에서 email 추출
   */
  public String getEmail(String token) {

    return parseClaim(token).getSubject();
  }

  /**
   * Request Header의 Cookie에서 토큰 추출 후 이메일 반환
   */
  public String getEmail(HttpServletRequest request){

    String token = resolveTokenFromRequest(request);

    if (!StringUtils.isNullOrEmpty(token)) {
      return getEmail(token);
    }

    return null;
  }

  /**
   * 토큰 유효성 확인
   */
  public boolean validateToken(String token) {

    if (StringUtils.isNullOrEmpty(token)) {
      return false;
    }

    Claims claims = parseClaim(token);

    return !claims.getExpiration().before(new Date());

  }

  /**
   * 토큰의 payload에서 Claim 추출
   */
  private Claims parseClaim(String token) {

    try {

      return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();

    } catch(ExpiredJwtException e) {

      return e.getClaims();

    }

  }

  /**
   * Request Header의 Cookie에서 토큰 추출
   */
  public String resolveTokenFromRequest(HttpServletRequest request) {

    Cookie[] cookies = request.getCookies();

    if (ObjectUtils.isEmpty(cookies)) {
      return null;
    }

    Optional<String> token = Arrays.stream(cookies)
        .filter(c -> c.getName().equals(TOKEN_HEADER))
        .map(c -> c.getValue())
        .findFirst();

    if (token.isPresent()) {
      return token.get().substring(TOKEN_PREFIX.length());
    }

    return null;
  }


  
}
