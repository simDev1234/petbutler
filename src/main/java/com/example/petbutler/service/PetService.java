package com.example.petbutler.service;

import com.example.petbutler.dto.PetDto;
import com.example.petbutler.entity.Customer;
import com.example.petbutler.exception.ButlerUserException;
import org.springframework.web.multipart.MultipartFile;

public interface PetService {
  boolean registerPetsWhenSignUp(Customer customer, PetDto[] petDtos, MultipartFile[] files) throws ButlerUserException;
}
