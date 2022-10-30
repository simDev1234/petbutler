package com.example.petbutler.service.impl;

import com.example.petbutler.dto.PetDto;
import com.example.petbutler.entity.Customer;
import com.example.petbutler.entity.Pet;
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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class PetServiceImpl implements PetService {

  private final PetRepository petRepository;

  private final FileUploadUtils fileUploadUtils;

  /**
   * 회원 가입 시 팻 등록
   */
  @Override
  @Transactional
  public void registerPetsWhenSignUp(Customer customer, PetDto[] petDtos, MultipartFile[] files) throws IOException {

    // copy thumbnail & transform data type
    Pet[] pets = Pet.from(customer, petDtos, copyAndPasteThumbnailFiles(files));

    // validation
    validateRegisterPetsWithSignUp(pets);
    
    // save pets
    for (Pet pet : pets){
      petRepository.save(pet);
    }

  }

  private static void validateRegisterPetsWithSignUp(Pet[] pets) {

    int count = (int) Arrays.stream(pets).filter(p -> p.getName() == null || "".equals(p.getName())).count();

    if (count > 0) {
      throw new ButlerUserException(ErrorCode.PET_REGISTER_NOT_ENOUGH_DATA);
    }

  }

  private List<FilePath> copyAndPasteThumbnailFiles(MultipartFile[] files) throws IOException {

    List<FilePath> filePaths = new ArrayList();

    if (Objects.nonNull(files)){

      for (MultipartFile file : files) {

        if (file.isEmpty()) {
          filePaths.add(new FilePath());
          continue;
        }

        FilePath filePath = fileUploadUtils.saveFile(file, file.getOriginalFilename(),
            "C:\\sebinSample\\petbutler\\src\\main\\resources\\static\\files", "/files");

        filePaths.add(filePath);

      }
    }

    return filePaths;
  }


}
