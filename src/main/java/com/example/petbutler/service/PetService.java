package com.example.petbutler.service;

import com.example.petbutler.dto.PetDto;
import com.example.petbutler.entity.Customer;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

public interface PetService {
  void registerPetsWhenSignUp(Customer customer, PetDto[] petDtos, MultipartFile[] files) throws IOException;
}
