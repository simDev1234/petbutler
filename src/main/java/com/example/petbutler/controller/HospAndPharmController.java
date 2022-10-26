package com.example.petbutler.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HospAndPharmController {

  /**
   * 동물 병원/약국 찾기 페이지로 이동
   */
  @GetMapping("/hospitalsAndPharmacy")
  public String map(){
    return "hospital-and-pharmacy";
  }
}
