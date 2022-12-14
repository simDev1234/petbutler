package com.example.petbutler.admin.web;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@PreAuthorize("hasRole('ADMIN')")
public class AdminMainController {

  /* 접근 권한 불허 시 denied page mapping */
  @RequestMapping("/error/access-denied")
  public String errorDenied() {
    return "exception/access-denied";
  }

  /* 관리자 메인 page mapping */
  @GetMapping("/admin/main.do")
  public String main(){
    return "admin/index";
  }

}
