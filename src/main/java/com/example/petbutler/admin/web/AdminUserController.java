package com.example.petbutler.admin.web;

import com.example.petbutler.admin.model.AdminUserDetailForm;
import com.example.petbutler.admin.model.AdminUserInfo;
import com.example.petbutler.admin.model.AdminUserListForm;
import com.example.petbutler.admin.service.AdminUserService;
import com.example.petbutler.model.constants.UserRole;
import com.example.petbutler.persist.UserRepository;
import com.example.petbutler.persist.entity.User;
import com.mysql.cj.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin/user")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminUserController {

  private final UserRepository userRepository;

  private final AdminUserService adminUserService;

  /**
   * 회원 목록 조회
   */
  @GetMapping("/list.do")
  public String getUserList(AdminUserListForm adminUserListForm,
                            @PageableDefault(sort = "registeredAt",
                                direction = Direction.DESC) Pageable pageable,
                            Model model) {

    Page<AdminUserInfo> pageResult = adminUserService.getAdminUserList(adminUserListForm, pageable);

    adminUserListForm.setPageResult(pageResult);

    model.addAttribute("data", adminUserListForm);

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
