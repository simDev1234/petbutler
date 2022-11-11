package com.example.petbutler.admin.controller;

import com.example.petbutler.admin.model.AdminCategoryForm;
import com.example.petbutler.admin.service.AdminCategoryService;
import com.example.petbutler.persist.entity.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/category")
@RequiredArgsConstructor
public class AdminCategoryController {

  private final AdminCategoryService adminCategoryService;

  /**
   * 카테고리 조회
   */
  @GetMapping("/list.do")
  public String getCategoryList(AdminCategoryForm form, Pageable pageable, Model model){

    form = adminCategoryService.getCategoryList(form, pageable);

    model.addAttribute("searchKey",     form.getSearchKey());
    model.addAttribute("searchValue",   form.getSearchValue());
    model.addAttribute("pageResult",    form.getPageResult());
    model.addAttribute("mains",         form.getMains());
    model.addAttribute("mediums",       form.getMediums());
    model.addAttribute("smalls",        form.getSmalls());
    model.addAttribute("productCntMap", form.getProductCntMap());

    return "admin/category/list";
  }


  /**
   * 카테고리 등록
   */
  @PostMapping("/create.do")
  public ResponseEntity createCategory(@RequestBody AdminCategoryForm form){

    Category category = adminCategoryService.createCategory(form);

    return ResponseEntity.ok(category);
  }


  /**
   * 카테고리 수정
   */
  @PatchMapping("/update.do")
  public ResponseEntity updateCategoryName(@RequestBody AdminCategoryForm form){

    Category category = adminCategoryService.updateCategoryName(form);

    return ResponseEntity.ok(category);
  }

  /**
   * 카테고리 삭제
   */
  @DeleteMapping("/delete.do/{code}")
  public ResponseEntity deleteCategory(@PathVariable String code){

    adminCategoryService.deleteCategory(code);

    return ResponseEntity.ok().build();
  }

}
