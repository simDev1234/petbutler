package com.example.petbutler.admin.service.impl;

import com.example.petbutler.admin.model.AdminUserDetailForm;
import com.example.petbutler.admin.service.AdminUserService;
import com.example.petbutler.exception.ButlerUserException;
import com.example.petbutler.exception.type.ErrorCode;
import com.example.petbutler.model.PetDetail;
import com.example.petbutler.persist.PetRepository;
import com.example.petbutler.persist.UserRepository;
import com.example.petbutler.persist.entity.User;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {

  private final UserRepository userRepository;

  private final PetRepository petRepository;

  /**
   * 회원 상세 조회
   * - 회원이 없는 경우 실패 응답
   * - 회원 정보와 더불어 해당 회원의 팻 정보 전달
   */
  @Transactional
  public AdminUserDetailForm getAdminUserDetail(String email) {

    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new ButlerUserException(ErrorCode.USER_NOT_FOUND));

    AdminUserDetailForm userDetail = AdminUserDetailForm.fromUserInfo(user);

    List<PetDetail> pets =
        petRepository.findAllByUser(user).stream().map(PetDetail::from).collect(Collectors.toList());

    if (!CollectionUtils.isEmpty(pets)) {
      userDetail.setPets(pets);
    }
    return userDetail;

  }

  /**
   * 회원 상세 수정 - 연락처, 회원 상태 및 역할(권한)만 변경할 수 있다.
   */
  @Transactional
  public void updateUserStatusAndUserRole(AdminUserDetailForm adminUserDetailForm) {

    User user = userRepository.findByEmail(adminUserDetailForm.getEmail())
        .orElseThrow(() -> new ButlerUserException(ErrorCode.USER_NOT_FOUND));

    user.setUserRole(adminUserDetailForm.getUserRole());
    user.setUserStatus(adminUserDetailForm.getUserStatus());
    user.setPhone(adminUserDetailForm.getPhone());

  }


}
