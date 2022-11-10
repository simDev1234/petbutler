package com.example.petbutler.security.authentication;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.example.petbutler.persist.entity.User;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtTokenUtils {

  private final UserDetailsService userDetailsService;

  private final String signKey = Base64.getEncoder().encodeToString("petbutler".getBytes());

  public String createToken(User user){

    LocalDateTime expiredDateTime = LocalDateTime.now().plusMonths(1);
    Date expiredDate = Timestamp.valueOf(expiredDateTime);

    return JWT.create()
        .withExpiresAt(expiredDate)
        .withClaim("user_id", user.getId())
        .withIssuer(user.getEmail())
        .sign(Algorithm.HMAC512(signKey));
  }

  public String getToken(HttpServletRequest request) {
    return request.getHeader("F-TOKEN");
  }

  public Authentication getAuthentication(String token) {

    UserDetails userDetails = userDetailsService.loadUserByUsername(getIssuer(token));

    return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
  }

  public String getIssuer(String token){

    String email = JWT.require(Algorithm.HMAC512(signKey))
                      .build()
                      .verify(token)
                      .getIssuer();

    return email;
  }

  public boolean isValid(String token) {

    try{
      JWT.require(Algorithm.HMAC512(signKey))
          .build()
          .verify(token);
    } catch(AlgorithmMismatchException e) {
      return false;
    } catch(SignatureVerificationException e) {
      return false;
    } catch(InvalidClaimException e) {
      return false;
    } catch(TokenExpiredException e) {
      return false;
    }

    return true;
  }

}
