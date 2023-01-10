package com.example.petbutler.web;

import com.example.petbutler.model.PetDetailForm;
import com.example.petbutler.model.PetRegisterForm;
import com.example.petbutler.service.PetService;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user/mypets")
@RequiredArgsConstructor
public class PetController {

  private final PetService petService;

  /**
   * 마이펫 - 팻 조회
   */
  @GetMapping
  public String getMyPets(Principal principal, Model model) {

    // Principal에서 아이디 추출
    String email = principal.getName();

    // 팻 정보
    List<PetDetailForm> pets = petService.getPetDetailByUserEmail(email);

    model.addAttribute("pets", pets);

    return "user/mypets";
  }

  /**
   * 마이펫 - 펫 등록 페이지 이동
   */
  @GetMapping("/register-pet")
  public String getRegisterPetPage(){

    return "user/register-pet";

  }

  /**
   * 마이펫 - 팻 등록
   */
  @PostMapping("/register-pet")
  public String registerPets(PetRegisterForm petRegisterForm, Principal principal, Model model){

    boolean petRegisterResult = petService.registerPetsAfterSignUp(principal.getName(), petRegisterForm);

    if (petRegisterResult) {

      model.addAttribute("popupTitle", "반려동물 등록 완료 안내");
      model.addAttribute("popupMsg", "반려동물 등록이 정상적으로 완료되었습니다.");

    } else {

      model.addAttribute("popupTitle", "반려동물 등록 에러 안내");
      model.addAttribute("popupMsg", "반려동물 등록이 정상적으로 이루어지지 않았습니다.");

    }

    return "user/register-pet";

  }

  /**
   * 마이펫 - 팻 수정
   */
  @PutMapping
  public ResponseEntity updatePet(@RequestBody PetDetailForm form){

    petService.updatePet(form);

    return ResponseEntity.ok().build();
  }

  /**
   * 마이펫 - 팻 삭제
   */
  @DeleteMapping("/{id}")
  public ResponseEntity deletePet(@PathVariable long id){

    petService.deletePet(id);

    return ResponseEntity.ok().build();
  }


}
