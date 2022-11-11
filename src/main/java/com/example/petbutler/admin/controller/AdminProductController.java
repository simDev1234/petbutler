package com.example.petbutler.admin.controller;

import com.example.petbutler.admin.model.AdminCategoryForm;
import com.example.petbutler.admin.model.AdminProductSearchForm;
import com.example.petbutler.admin.service.AdminCategoryService;
import com.example.petbutler.admin.service.AdminProductServiceImpl;
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
@RequestMapping("/admin/product")
@RequiredArgsConstructor
public class AdminProductController {

  private final AdminProductServiceImpl adminProductService;

  /**
   * 상품 조회
   */
  @GetMapping("/list.do")
  public String getProductList(AdminProductSearchForm form, Pageable pageable, Model model){

    form = adminProductService.getProductList(form, pageable);

    model.addAttribute("data", form);

    return "admin/product/list";
  }


  /**
   * 상품 등록
   */
  @PostMapping("/create.do")
  public ResponseEntity createCategory(){



    return null;
  }


  /**
   * 상품 수정
   */
  @PatchMapping("/update.do")
  public ResponseEntity updateCategoryName(){



    return null;
  }

  /**
   * 상품 삭제
   */
  @DeleteMapping("/delete.do/{id}")
  public ResponseEntity deleteCategory(){



    return null;
  }

}
