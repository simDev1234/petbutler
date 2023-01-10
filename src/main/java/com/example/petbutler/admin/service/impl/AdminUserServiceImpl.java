package com.example.petbutler.admin.service.impl;

import com.example.petbutler.admin.model.AdminUserDetailForm;
import com.example.petbutler.admin.model.AdminUserInfo;
import com.example.petbutler.admin.model.AdminUserListForm;
import com.example.petbutler.admin.service.AdminUserService;
import com.example.petbutler.exception.ButlerUserException;
import com.example.petbutler.exception.constants.ErrorCode;
import com.example.petbutler.model.PetDetailForm;
import com.example.petbutler.model.constants.UserRole;
import com.example.petbutler.model.constants.UserStatus;
import com.example.petbutler.persist.PetRepository;
import com.example.petbutler.persist.UserRepository;
import com.example.petbutler.persist.entity.User;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {

  private final UserRepository userRepository;

  private final PetRepository petRepository;

  /**
   * 회원 리스트 조회
   */
  @Override
  public Page<AdminUserInfo> getAdminUserList(AdminUserListForm form, Pageable pageable) {

    // 검색을 할 때
    if (Objects.nonNull(form.getSearchKey())) {

      String searchKey   = form.getSearchKey();
      String searchValue = form.getSearchValue();

      // 전체 회원 중 검색
      if ("all".equals(searchKey)){

        if (Objects.nonNull(searchValue)) {
          return userRepository.findAllByEmailContains(pageable, searchValue)
                     .map(AdminUserInfo::fromEntity);
        } else {
          return userRepository.findAll(pageable).map(AdminUserInfo::fromEntity);
        }

      }
      // 일반 회원 또는 관리자에서 검색
      else {

        UserRole role = UserRole.valueOf(String.format("ROLE_%s", searchKey.toUpperCase()));

        return userRepository.findAllByUserRoleAndEmailContains(pageable, role, searchValue)
            .map(AdminUserInfo::fromEntity);

      }

    }

    // 검색을 하지 않을 때
    return userRepository.findAll(pageable).map(AdminUserInfo::fromEntity);
  }

  /**
   * 회원 상세 조회
   * - 회원이 없는 경우 실패 응답
   * - 회원 정보와 더불어 해당 회원의 팻 정보 전달
   */
  @Transactional
  public AdminUserDetailForm getAdminUserDetail(String email) {

    // User 정보 DB에서 조회
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new ButlerUserException(ErrorCode.USER_NOT_FOUND));

    // User -> AdminUserDetailForm
    AdminUserDetailForm userDetailForm = AdminUserDetailForm.fromEntity(user);

    // Pet 정보 DB에서 조회
    List<PetDetailForm> pets =
        petRepository.findAllByUser(user).stream().map(PetDetailForm::from).collect(Collectors.toList());

    // Pet 정보 추가
    if (!CollectionUtils.isEmpty(pets)) {
      userDetailForm.setPets(pets);
    }

    return userDetailForm;

  }

  /**
   * 회원 상세 수정
   * - 연락처, 회원 상태 및 역할(권한)만 변경할 수 있다.
   * - 등록된 회원이 없는 경우 실패 응답
   */
  @Transactional
  public void updateUserStatusAndUserRole(AdminUserDetailForm adminUserDetailForm) {

    User user = userRepository.findByEmail(adminUserDetailForm.getEmail())
        .orElseThrow(() -> new ButlerUserException(ErrorCode.USER_NOT_FOUND));

    user.setPhone(adminUserDetailForm.getPhone());
    user.setUserRole(UserRole.valueOf(adminUserDetailForm.getUserRole()));
    user.setUserStatus(UserStatus.valueOf(adminUserDetailForm.getUserStatus()));

  }


}
