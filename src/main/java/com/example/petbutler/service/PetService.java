package com.example.petbutler.service;

import com.example.petbutler.model.PetDetailForm;
import com.example.petbutler.model.PetRegisterForm;
import com.example.petbutler.persist.entity.User;
import java.util.List;

public interface PetService {
  void registerPetsWhenSignUp(User user, PetRegisterForm petRegisterForm);

  boolean registerPetsAfterSignUp(String email, PetRegisterForm petRegisterForm);

  List<PetDetailForm> getPetDetailByUserEmail(String email);

  void updatePet(PetDetailForm form);

  void deletePet(long id);
}
