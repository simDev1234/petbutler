package com.example.petbutler.admin.controller;

import com.example.petbutler.admin.model.AdminUserDetailForm;
import com.example.petbutler.admin.model.AdminUserSearchForm;
import com.example.petbutler.admin.service.AdminUserService;
import com.example.petbutler.persist.UserRepository;
import com.example.petbutler.persist.entity.User;
import com.example.petbutler.type.UserRole;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin/user")
@RequiredArgsConstructor
public class AdminUserController {

  private final UserRepository userRepository;

  private final AdminUserService adminUserService;

  /**
   * 회원 목록 조회
   */
  @GetMapping("/list.do")
  public String getUserList(AdminUserSearchForm adminUserSearchForm,
                            @PageableDefault(sort = "registeredAt",
                                direction = Direction.DESC) Pageable pageable,
                            Model model) {

    Page<User> pageResult;
    String searchKey = adminUserSearchForm.getSearchKey();
    String searchValue = adminUserSearchForm.getSearchValue();

    // 검색어 입력 시 필터링
    if (Objects.nonNull(searchKey)) {
      // 구분 : 전체
      if ("all".equals(searchKey)) {
        pageResult = userRepository.findAllByEmailContainsIgnoreCase(searchValue, pageable);
      } 
      // 구분 : 일반 회원 or 관리자
      else {
        UserRole userRole = UserRole.findUserRole(String.format("ROLE_%s", searchValue.toUpperCase()));
        pageResult = userRepository.findAllByUserRoleAndEmailContainsIgnoreCase(userRole, searchValue,
            pageable);
      }
      model.addAttribute("searchKey", searchKey);
      model.addAttribute("searchValue", searchValue);
    }
    // 아닐 경우 전체 중 현재페이지부터 10개
    else {
      pageResult = userRepository.findAll(pageable);
    }

    model.addAttribute("pageResult", pageResult);

    return "admin/user/list";
  }

  /**
   * 회원 상세 조회
   */
  @GetMapping("/detail.do")
  public String getUserDetail(@RequestParam String email, Model model) {

    AdminUserDetailForm userDetail = adminUserService.getAdminUserDetail(email);

    model.addAttribute("user", userDetail);

    return "admin/user/detail";
  }


  /**
   * 회원 상세 수정 - 연락처, 회원 상태 및 역할(권한)만 변경할 수 있다.
   */
  @PatchMapping("/update.do")
  public ResponseEntity<String> updateUserStatusAndRole(@RequestBody AdminUserDetailForm adminUserDetailForm) {

    adminUserService.updateUserStatusAndUserRole(adminUserDetailForm);

    return ResponseEntity.ok(adminUserDetailForm.getEmail());
  }



}