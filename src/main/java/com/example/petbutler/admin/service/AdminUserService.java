package com.example.petbutler.admin.service;

import com.example.petbutler.admin.model.AdminUserDetailForm;
import com.example.petbutler.admin.model.AdminUserInfo;
import com.example.petbutler.admin.model.AdminUserListForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminUserService {

  Page<AdminUserInfo> getAdminUserList(AdminUserListForm adminUserListForm, Pageable pageable);

  AdminUserDetailForm getAdminUserDetail(String email);

  void updateUserStatusAndUserRole(AdminUserDetailForm adminUserDetailForm);

}
