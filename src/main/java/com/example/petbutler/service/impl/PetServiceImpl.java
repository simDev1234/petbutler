package com.example.petbutler.service.impl;

import com.example.petbutler.config.ServerPropertyConfig;
import com.example.petbutler.dto.PetRegisterForm;
import com.example.petbutler.entity.Customer;
import com.example.petbutler.entity.Pet;
import com.example.petbutler.exception.ButlerPetException;
import com.example.petbutler.exception.ButlerUserException;
import com.example.petbutler.exception.type.ErrorCode;
import com.example.petbutler.repository.PetRepository;
import com.example.petbutler.service.PetService;
import com.example.petbutler.utils.FilePath;
import com.example.petbutler.utils.FileUploadUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class PetServiceImpl implements PetService {

  private final PetRepository petRepository;

  private final ServerPropertyConfig serverPropertyConfig;
  private final FileUploadUtils fileUploadUtils;

  /**
   * 회원 가입 시 팻 등록
   */
  @Override
  @Transactional(rollbackFor = IOException.class)
  public void registerPetsWhenSignUp(Customer customer, PetRegisterForm petRegisterForm) throws IOException {

    List<FilePath> newFilePaths = copyAndPasteThumbnailFiles(petRegisterForm.getThumbnail());

    List<Pet> pets = petRegisterForm.toPets(customer, newFilePaths)
        .stream().filter(p->Objects.nonNull(p.getName())).collect(Collectors.toList());

    pets.stream().forEach(
        pet -> petRepository.save(pet)
    );

  }

  private List<FilePath> copyAndPasteThumbnailFiles(MultipartFile[] files) throws IOException {

    String localRoot = serverPropertyConfig.getPetThumbnailLocalRoot();
    String urlRoot   = serverPropertyConfig.getPetThumbnailUrlRoot();

    List<FilePath> filePaths = new ArrayList();

    for (MultipartFile file : files) {

      if (Objects.nonNull(file)) {
        FilePath filePath
            = fileUploadUtils.saveFile(file, file.getOriginalFilename(), localRoot, urlRoot);
        filePaths.add(filePath);
      } else {
        filePaths.add(new FilePath("",""));
      }

    }

    return filePaths;
  }


}
