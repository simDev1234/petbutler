package com.example.petbutler.service;

import com.example.petbutler.model.PetRegisterForm;
import com.example.petbutler.persist.entity.User;

public interface PetService {
  void registerPetsWhenSignUp(User user, PetRegisterForm petRegisterForm);

  boolean registerPetsAfterSignUp(String email, PetRegisterForm petRegisterForm);
}
