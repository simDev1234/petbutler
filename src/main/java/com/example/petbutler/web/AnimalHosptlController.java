package com.example.petbutler.web;

import com.example.petbutler.model.AnimalHosptlApiForm;
import com.example.petbutler.service.AnimalHosptlService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/map/hospital")
@RequiredArgsConstructor
public class AnimalHosptlController {

  private final AnimalHosptlService animalHosptlService;

  @GetMapping
  public String getHospitalMap(@RequestBody AnimalHosptlApiForm form){

    // 동물 병원 정보 가져오기


    return "map/map-hospital";
  }

  @PostMapping
  public void loadHospitalData(){

    animalHosptlService.loadHospitalData();

  }

}
