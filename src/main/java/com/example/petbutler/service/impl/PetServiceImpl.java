package com.example.petbutler.service.impl;

import com.example.petbutler.exception.ButlerPetException;
import com.example.petbutler.exception.ButlerUserException;
import com.example.petbutler.exception.constants.ErrorCode;
import com.example.petbutler.model.PetDetailForm;
import com.example.petbutler.model.PetRegisterForm;
import com.example.petbutler.persist.PetRepository;
import com.example.petbutler.persist.UserRepository;
import com.example.petbutler.persist.entity.Pet;
import com.example.petbutler.persist.entity.User;
import com.example.petbutler.service.PetService;
import com.example.petbutler.utils.FileUploadUtils;
import com.example.petbutler.utils.wrapper.FilePath;
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

@Service
@RequiredArgsConstructor
@Slf4j
public class PetServiceImpl implements PetService {

  private final PetRepository petRepository;

  private final UserRepository userRepository;

  private final FileUploadUtils fileUploadUtils;

  /**
   * 회원 가입 시 팻 등록
   * - 팻 정보가 하나도 없거나, 모든 팻 정보가 이름이 누락된 경우 실패응답
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
   * 회원 가입 후 팻 등록 (마이펫의 등록페이지)
   * - 팻 정보가 하나도 없거나, 모든 팻 정보가 이름이 누락된 경우 실패응답
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

    return count != 0;
  }

  private List<Pet> copyThumbnailAndGetPets(User user, PetRegisterForm petRegisterForm) {

    // copy thumbnail files
    List<FilePath> newFilePaths = fileUploadUtils.copyAndPasteThumbnailFiles(petRegisterForm.getThumbnail());

    // form -> pet
    List<Pet> pets = new ArrayList<>();

    if (!CollectionUtils.isEmpty(newFilePaths)) {
      pets = petRegisterForm.toPets(user, newFilePaths)
          .stream().filter(p->Objects.nonNull(p.getName())).collect(Collectors.toList());
    }

    return pets;

  }

  /**
   * 마이팻 리스트 조회
   */
  @Override
  @Transactional
  public List<PetDetailForm> getPetDetailByUserEmail(String email) {

    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new ButlerUserException(ErrorCode.USER_NOT_FOUND));

    List<Pet> pets = petRepository.findAllByUser(user);

    if (!CollectionUtils.isEmpty(pets)){
      return pets.stream().map(PetDetailForm::from).collect(Collectors.toList());
    }

    return null;

  }

  /**
   * 마이팻 수정
   */
  @Override
  @Transactional
  public void updatePet(PetDetailForm form) {

    Pet pet = petRepository.findById(form.getId())
        .orElseThrow(() -> new ButlerPetException(ErrorCode.PET_NOT_FOUND));

    pet.setKind(form.getKind());
    pet.setName(form.getName());

  }

  /**
   * 마이팻 삭제
   */
  @Override
  @Transactional
  public void deletePet(long id) {

    validateIfPetExist(id);

    petRepository.deleteById(id);

  }

  private void validateIfPetExist(long id) {
    if (!petRepository.existsById(id)) {
      throw new ButlerPetException(ErrorCode.PET_NOT_FOUND);
    }
  }
}
