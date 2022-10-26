package com.example.petbutler.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProductController {

  /**
   * 스토어 페이지 이동
   */
  @GetMapping("/store")
  public String store(){
    return "store";
  }
}
