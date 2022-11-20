package com.example.petbutler.admin.web;

import com.example.petbutler.admin.model.AdminAnimalHosptlApiForm;
import com.example.petbutler.admin.model.AdminProductAddForm;
import com.example.petbutler.admin.model.AdminProductSearchForm;
import com.example.petbutler.admin.service.AdminAnimalHosptlService;
import com.example.petbutler.admin.service.AdminProductService;
import com.example.petbutler.persist.entity.AnimalHosptl;
import com.example.petbutler.persist.entity.Product;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/animalHosptl")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminHosptlController {

  private final AdminAnimalHosptlService adminAnimalHosptlService;

  private final AdminProductService adminProductService;

  /**
   * 동물병원 조회
   */
  @GetMapping("/list.do")
  public String getAnimalHosptlList(AdminAnimalHosptlApiForm form, Model model, Pageable pageable){

    Page<AnimalHosptl> pageResult = adminAnimalHosptlService.getAnimalHosptlList(form, pageable);

    form.setPageResult(pageResult);

    model.addAttribute("data", form);

    return "admin/animalHosptl/list";
  }

  /**
   * 상품 팝업
   */
  @GetMapping("/product-popup.do")
  public String getProductList(AdminProductSearchForm form, Pageable pageable, Model model){

    form = adminProductService.getProductList(form, pageable);

    model.addAttribute("data", form);

    return "admin/animalHosptl/pop-up";
  }

  /**
   * 동물병원에 상품 등록
   */
  @PutMapping("/product-add.do")
  public ResponseEntity<?> addProduct(@RequestBody AdminProductAddForm form){
        
    // 해당 상품 가져오기
    Product product = adminProductService.findProductById(form.getProductId());

    // 병원에 상품 등록하기
    adminAnimalHosptlService.addProductToAnimalHosptl(form.getHosptlId(), product);

    return ResponseEntity.ok().build();
  }

  /**
   * 동물병원 보유상품 보기
   */
  @GetMapping("/product-show.do/{id}")
  public ResponseEntity<?> showProducts(@PathVariable long id){

    // 병원 상품 가져오기
    List<Product> products = adminAnimalHosptlService.getProductsFromHospl(id);

    // 상품 이름만 문자열로 파싱하기
    StringBuilder sb = new StringBuilder();

    sb.append(products.get(0).getName());
    products.stream().limit(products.size()-1)
        .forEach(p -> sb.append(String.format(", %s", p.getName())));

    return ResponseEntity.ok(sb.toString());
  }

}
