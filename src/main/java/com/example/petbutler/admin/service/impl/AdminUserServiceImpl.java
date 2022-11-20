package com.example.petbutler.admin.service.impl;

import static com.example.petbutler.model.constants.UserRole.ROLE_ADMIN;

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
import com.mysql.cj.util.StringUtils;
import java.util.List;
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

    // 이메일 검색을 할 때
    if (!StringUtils.isNullOrEmpty(form.getSearchKey())
        && StringUtils.isNullOrEmpty(form.getSearchValue())) {

      // 회원 유형, 이메일 검색어
      String role        = form.getSearchKey();
      String emailSearch = form.getSearchValue();

      // 회원 유형 구분이 없을 때
      if ("all".equals(role)){

        return userRepository.findAllByEmailContainsIgnoreCase(pageable, emailSearch)
                             .map(c -> AdminUserInfo.fromEntity(c));

      } 
      // 회원 유형 구분이 있을 때
      else {

        return isAdmin(role)?
            userRepository.findAllByUserRolesContainingAndEmailContainsIgnoreCase(pageable, ROLE_ADMIN, emailSearch)
                          .map(c -> AdminUserInfo.fromEntity(c)) :
            userRepository.findAllByUserRolesNotContainingAndEmailContainsIgnoreCase(pageable, ROLE_ADMIN, emailSearch)
                          .map(c -> AdminUserInfo.fromEntity(c));

      }

    }

    // 이메일 검색을 하지 않을 때
    return userRepository.findAll(pageable).map(c -> AdminUserInfo.fromEntity(c));
  }

  private boolean isAdmin(String searchValue) {

    return "admin".equals(searchValue) ? true : false;

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
    AdminUserDetailForm userDetailForm = user.toUserDetailForm();

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
   * 회원 상세 수정 - 연락처, 회원 상태 및 역할(권한)만 변경할 수 있다.
   */
  @Transactional
  public void updateUserStatusAndUserRole(AdminUserDetailForm adminUserDetailForm) {

    User user = userRepository.findByEmail(adminUserDetailForm.getEmail())
        .orElseThrow(() -> new ButlerUserException(ErrorCode.USER_NOT_FOUND));

    user.setUserRoles(adminUserDetailForm.getRoles());

    if (UserStatus.IN_USE.equals(user.getUserStatus())) {
      user.authorize();
    } else {
      user.setUserStatus(adminUserDetailForm.getUserStatus());
    }

    user.setPhone(adminUserDetailForm.getPhone());

  }


}
