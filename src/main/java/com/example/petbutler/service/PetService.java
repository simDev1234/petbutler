package com.example.petbutler.service;

import com.example.petbutler.dto.PetRegisterForm;
import com.example.petbutler.entity.Customer;
import java.io.IOException;

public interface PetService {
  void registerPetsWhenSignUp(Customer customer, PetRegisterForm petRegisterForm) throws IOException;
}
