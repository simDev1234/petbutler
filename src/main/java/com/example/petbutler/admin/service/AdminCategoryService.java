package com.example.petbutler.admin.service;

import com.example.petbutler.admin.model.AdminCategoryForm;
import com.example.petbutler.persist.entity.Category;
import org.springframework.data.domain.Pageable;

public interface AdminCategoryService {

  Category createCategory(AdminCategoryForm form);

  Category updateCategoryName(AdminCategoryForm form);

  void deleteCategory(String code);

  AdminCategoryForm getCategoryList(AdminCategoryForm form, Pageable pageable);
}
