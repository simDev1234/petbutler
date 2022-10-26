package com.example.petbutler.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PostingController {

  /**
   * 포스팅 페이지 이동
   */
  @GetMapping("/postings")
  public String postings(){
    return "posting";
  }
}
