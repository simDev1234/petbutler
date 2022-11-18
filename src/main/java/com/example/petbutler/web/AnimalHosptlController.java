package com.example.petbutler.web;

import com.example.petbutler.service.AnimalHosptlService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/map/hospital")
@RequiredArgsConstructor
public class AnimalHosptlController {

  private final AnimalHosptlService animalHosptlService;

  @GetMapping
  public String getHospitalMap(){

    return "map/map-hospital";
  }

  @PostMapping
  public void loadHospitalData(){

    animalHosptlService.loadHospitalData();

  }

}
