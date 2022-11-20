package com.example.petbutler.admin.service.impl;

import com.example.petbutler.admin.model.AdminProductSearchForm;
import com.example.petbutler.admin.service.AdminProductService;
import com.example.petbutler.exception.ButlerProductException;
import com.example.petbutler.exception.constants.ErrorCode;
import com.example.petbutler.persist.CategoryRepository;
import com.example.petbutler.persist.ProductRepository;
import com.example.petbutler.persist.entity.Category;
import com.example.petbutler.persist.entity.Product;
import com.example.petbutler.model.constants.Division;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminProductServiceImpl implements AdminProductService {

  private final ProductRepository productRepository;

  private final CategoryRepository categoryRepository;

  /**
   * 상품 조회
   */
  @Transactional
  public AdminProductSearchForm getProductList(AdminProductSearchForm form, Pageable pageable) {

    // 검색 필터링에 따른 페이징 결과
    setPageResultBySearchFilter(form, pageable);

    // 분류별 카테고리 리스트 (검색 시 사용)
    setDivisionList(form);

    return form;

  }

  
  private void setPageResultBySearchFilter(AdminProductSearchForm form, Pageable pageable) {

    // 검색 필터링X : 상품분류 미선택, 검색어 입력 X
    if (Objects.isNull(form.getDivision()) && Objects.isNull(form.getSearchValue())) {
      form.setPageResult(productRepository.findAll(pageable));
    }

    // 검색 필터링을 걸은 경우
    String searchKey    = form.getSearchKey();
    String searchValue  = form.getSearchValue();

    if ("name".equals(searchKey)) {

      form.setPageResult(findAllByCategoryCodeAndName(form.getDivision(), form.getCategoryCode(), searchValue, pageable));

      System.out.println(findAllByCategoryCodeAndName(form.getDivision(), form.getCategoryCode(), searchValue, pageable).toString());

    } else if ("brand".equals(searchKey)){

      form.setPageResult(findAllByCategoryCodeAndBrand(form.getDivision(), form.getCategoryCode(), searchValue, pageable));

    }

  }

  private Page<Product> findAllByCategoryCodeAndName(Division division, String categoryCode, String name, Pageable pageable) {

    // 대분류만 검색
    if (Division.MAIN.equals(division)) {
      return productRepository.findAllByCategoryMainCodeAndNameContaining(categoryCode, name, pageable);
    }
    // 중분류까지 검색
    else if (Division.MEDIUM.equals(division)) {
      return productRepository.findAllByCategoryMediumCodeAndNameContaining(categoryCode, name, pageable);
    }
    // 소분류까지 검색
    else if (Division.SMALL.equals(division)){
      return productRepository.findAllByCategorySmallCodeAndNameContaining(categoryCode, name, pageable);
    }
    // 아무 분류도 선택하지 않은 경우
    else {
      return productRepository.findAllByNameContaining(name, pageable);
    }
  }

  private Page<Product> findAllByCategoryCodeAndBrand(Division division, String categoryCode,
                                                      String brand, Pageable pageable){

    // 대분류만 검색
    if (Division.MAIN.equals(division)) {
      return productRepository.findAllByCategoryMainCodeAndBrandContaining(categoryCode, brand, pageable);
    }
    // 중분류까지 검색
    else if (Division.MEDIUM.equals(division)) {
      return productRepository.findAllByCategoryMediumCodeAndBrandContaining(categoryCode, brand, pageable);
    }
    // 소분류까지 검색
    else if (Division.SMALL.equals(division)){
      return productRepository.findAllByCategorySmallCodeAndBrandContaining(categoryCode, brand, pageable);
    }
    // 아무 분류도 선택하지 않은 경우
    else {
      return productRepository.findAllByBrandContaining(brand, pageable);
    }
  }

  public void setDivisionList(AdminProductSearchForm form) {

    List<Category> mains   = categoryRepository.findAllByDivision(Division.MAIN);
    List<Category> mediums = categoryRepository.findAllByDivision(Division.MEDIUM);
    List<Category> smalls  = categoryRepository.findAllByDivision(Division.SMALL);

    form.setMains(mains);
    form.setMediums(mediums);
    form.setSmalls(smalls);

  }

  /**
   * 특정 상품 조회
   */
  public Product findProductById(long productId) {

    return productRepository.findById(productId)
        .orElseThrow(() -> new ButlerProductException(ErrorCode.PRODUCT_NOT_FOUND));

  }
}
