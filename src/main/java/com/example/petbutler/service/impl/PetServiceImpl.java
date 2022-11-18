package com.example.petbutler.service.impl;

import com.example.petbutler.config.ServerConfig;
import com.example.petbutler.model.PetRegisterForm;
import com.example.petbutler.persist.entity.User;
import com.example.petbutler.persist.entity.Pet;
import com.example.petbutler.exception.ButlerUserException;
import com.example.petbutler.exception.constants.ErrorCode;
import com.example.petbutler.persist.UserRepository;
import com.example.petbutler.persist.PetRepository;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class PetServiceImpl implements PetService {

  private final PetRepository petRepository;

  private final UserRepository userRepository;

  private final ServerConfig serverConfig;
  private final FileUploadUtils fileUploadUtils;

  /**
   * 회원 가입 시 팻 등록 - 팻 정보가 하나도 없거나, 모든 팻 정보가 이름이 누락된 경우 실패응답
   */
  @Override
  @Transactional
  public void registerPetsWhenSignUp(User user, PetRegisterForm petRegisterForm) {

    // 팻 정보가 하나도 없거나, 팻 이름이 누락되었는지 확인
    if (validateRegisterPetBeforeCopyFile(petRegisterForm)){

      // 썸네일 복사
      List<Pet> pets = copyThumbnailAndGetPets(user, petRegisterForm);

      // 팻 저장
      if (!CollectionUtils.isEmpty(pets)){
        petRepository.saveAll(pets);
      }

    }
  }

  /**
   * 회원 가입 후 팻 등록
   */
  @Override
  @Transactional
  public boolean registerPetsAfterSignUp(String email, PetRegisterForm petRegisterForm) {

    // 팻 정보가 하나도 없거나, 팻 이름이 누락되었는지 확인
    if (!validateRegisterPetBeforeCopyFile(petRegisterForm)){
      return false;
    }

    // 고객 정보 찾기
    User user = userRepository.findByEmail(email)
                      .orElseThrow(() -> new ButlerUserException(ErrorCode.USER_NOT_FOUND));

    // 팻 신규 등록
    List<Pet> pets = copyThumbnailAndGetPets(user, petRegisterForm);

    if (!CollectionUtils.isEmpty(pets)) {

      petRepository.saveAll(pets);

    }

    return true;
  }

  private static boolean validateRegisterPetBeforeCopyFile(PetRegisterForm petRegisterForm) {

    if (Objects.isNull(petRegisterForm)) {
      return false;
    }

    if (Objects.isNull(petRegisterForm.getName())) {
      return false;
    }

    int count = (int)Arrays.stream(petRegisterForm.getName()).filter(e -> e != null && !e.equals("")).count();

    if (count == 0) {
      return false;
    }

    return true;
  }

  private List<Pet> copyThumbnailAndGetPets(User user, PetRegisterForm petRegisterForm) {

    // copy thumbnail files
    List<FilePath> newFilePaths = copyAndPasteThumbnailFiles(petRegisterForm.getThumbnail());

    // form -> pet
    List<Pet> pets = new ArrayList<>();

    if (!CollectionUtils.isEmpty(newFilePaths)) {
      pets = petRegisterForm.toPets(user, newFilePaths)
          .stream().filter(p->Objects.nonNull(p.getName())).collect(Collectors.toList());
    }

    return pets;

  }

  private List<FilePath> copyAndPasteThumbnailFiles(MultipartFile[] files) {

    List<FilePath> filePaths = new ArrayList();

    if (Objects.nonNull(files)) {

      String localRoot = serverConfig.getPetThumbnailLocalRoot();
      String urlRoot   = serverConfig.getPetThumbnailUrlRoot();

      for (MultipartFile file : files) {

        try{

          if (Objects.nonNull(file)) {
            FilePath filePath
                = fileUploadUtils.saveFile(file, file.getOriginalFilename(), localRoot, urlRoot);
            filePaths.add(filePath);
          } else {
            filePaths.add(new FilePath("",""));
          }

        } catch(IOException e) {

          log.info(e.getMessage());

        }
      }
    }

    return filePaths;
  }


}
