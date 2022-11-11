package com.example.petbutler.security;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      FilterChain chain, Authentication authentication) throws IOException, ServletException {
    super.onAuthenticationSuccess(request, response, chain, authentication);

    HttpSession session = request.getSession();

    if (session != null) {
      String redirectUrl = (String) session.getAttribute("prevPage");

      if (redirectUrl != null) {
        session.removeAttribute("prevPage");
        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
      } else {
        super.onAuthenticationSuccess(request, response, authentication);
      }
    } else {
      super.onAuthenticationSuccess(request, response, authentication);
    }

  }
}
