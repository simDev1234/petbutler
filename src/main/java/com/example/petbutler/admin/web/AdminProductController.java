package com.example.petbutler.admin.web;

import com.example.petbutler.admin.model.AdminProductSaveForm;
import com.example.petbutler.admin.model.AdminProductSearchForm;
import com.example.petbutler.admin.service.impl.AdminProductServiceImpl;
import com.example.petbutler.persist.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/product")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminProductController {

  private final AdminProductServiceImpl adminProductService;

  /**
   * 상품 목록
   */
  @GetMapping("/list.do")
  public String getProductList(AdminProductSearchForm form, Pageable pageable, Model model){

    form = adminProductService.getProductList(form, pageable);

    model.addAttribute("data", form);

    return "admin/product/list";
  }

  /**
   * 상품 상세 조회
   */
  @GetMapping("/detail.do/{id}")
  public String getProductDetail(@PathVariable Long id, Model model) {

    Product product = adminProductService.getProductDetail(id);

    AdminProductSearchForm form = adminProductService.getDivisionList();

    model.addAttribute("category", form);
    model.addAttribute("product", product);

    return "admin/product/detail";
  }

  /**
   * 상품 등록 페이지로 이동
   */
  @GetMapping("/add.do")
  public String getAddPage(Model model) {

    AdminProductSearchForm form = adminProductService.getDivisionList();

    model.addAttribute("data", form);

    return "admin/product/add";
  }

  /**
   * 상품 등록
   */
  @PostMapping("/add.do")
  public String addProduct(AdminProductSaveForm form, Model model){

    adminProductService.addProduct(form);

    return "redirect:/admin/product/list.do";
  }


  /**
   * 상품 수정
   */
  @PutMapping("/update.do")
  public String updateProduct(AdminProductSaveForm form){

    adminProductService.updateProduct(form);

    return "redirect:/admin/product/detail.do/" + form.getProductId();
  }

  /**
   * 상품 삭제
   */
  @DeleteMapping("/delete.do/{id}")
  public String deleteProduct(@PathVariable Long id){

    adminProductService.deleteProduct(id);

    return "redirect:/admin/product/list.do";
  }

}
