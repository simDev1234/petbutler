package com.example.petbutler.web;

import com.example.petbutler.model.AnimalHosptlApiForm;
import com.example.petbutler.persist.entity.AnimalHosptl;
import com.example.petbutler.service.AnimalHosptlService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/map/hospital")
@RequiredArgsConstructor
public class AnimalHosptlController {

  private final AnimalHosptlService animalHosptlService;

  @GetMapping
  public String getHosptlList(AnimalHosptlApiForm form, Model model){

    // 가까운 10군데 동물 병원 정보 가져오기
    List<AnimalHosptl> hosptls = animalHosptlService.getHosptlList(form);

    model.addAttribute("currentLat", form.getCurrentLat());
    model.addAttribute("currentLon", form.getCurrentLon());
    model.addAttribute("hosptls", hosptls);

    return "map/map-hospital";
  }



}
