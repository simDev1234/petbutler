package com.example.petbutler.admin.service;

import com.example.petbutler.admin.model.AdminUserDetailForm;

public interface AdminUserService {

  AdminUserDetailForm getAdminUserDetail(String email);

  void updateUserStatusAndUserRole(AdminUserDetailForm adminUserDetailForm);

}
