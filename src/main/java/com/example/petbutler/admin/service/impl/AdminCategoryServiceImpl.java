package com.example.petbutler.admin.service.impl;

import static com.example.petbutler.model.constants.Division.MAIN;
import static com.example.petbutler.model.constants.Division.MEDIUM;

import com.example.petbutler.admin.model.AdminCategoryForm;
import com.example.petbutler.admin.service.AdminCategoryService;
import com.example.petbutler.exception.ButlerCategoryException;
import com.example.petbutler.exception.constants.ErrorCode;
import com.example.petbutler.persist.CategoryRepository;
import com.example.petbutler.persist.ProductRepository;
import com.example.petbutler.persist.entity.Category;
import com.example.petbutler.model.constants.Division;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminCategoryServiceImpl implements AdminCategoryService {

  private final CategoryRepository categoryRepository;

  private final ProductRepository productRepository;

  /**
   * 카테고리 조회
   */
  @Override
  @Transactional
  public AdminCategoryForm getCategoryList(AdminCategoryForm form, Pageable pageable) {

    // 검색 필터링에 따른 페이징 결과
    setPageResultBySearchKeyAndValue(form, pageable);

    // 분류별 카테고리 리스트 (등록 시 사용)
    setDivisionList(form);

    // 카테고리 코드별 상품갯수
    setProductCntMap(form);

    return form;
  }

  private void setProductCntMap(AdminCategoryForm form) {

    Map<String, Integer> result = new HashMap();

    // 현재 페이지에서 노출할 카테고리들 중
    List<Category> categories = form.getPageResult().getContent();

    // 각 분류별 카테고리 코드 영역이 일치하는 상품의 갯수 정리
    categories.forEach(c -> {

      Division division = c.getDivision();
      String code = c.getCode();

      if (MAIN.equals(division)) {

        result.put(code, productRepository.countAllByCategoryMainCode(code));

      } else if (MEDIUM.equals(division)) {

        result.put(code, productRepository.countAllByCategoryMediumCode(code));

      } else {

        result.put(code, productRepository.countAllByCategorySmallCode(code));

      }

    });

    form.setProductCntMap(result);

  }

  public void setDivisionList(AdminCategoryForm form) {
    
    List<String> mains   = categoryRepository.findDistinctNameByDivision(MAIN.name());
    List<String> mediums = categoryRepository.findDistinctNameByDivision(MEDIUM.name());
    List<String> smalls  = categoryRepository.findDistinctNameByDivision(Division.SMALL.name());

    form.setMains(mains);
    form.setMediums(mediums);
    form.setSmalls(smalls);
    
  }

  private void setPageResultBySearchKeyAndValue(AdminCategoryForm form, Pageable pageable) {

    String searchKey = form.getSearchKey();
    String searchValue = form.getSearchValue();

    // 분류 미선택 또는 다른 경로에서 진입한 경우
    if (Objects.isNull(searchKey) || "all".equals(searchKey)) {

      form.setPageResult(categoryRepository.findAllByNameContaining(searchValue, pageable));

    }
    // 분류 선택하고
    else {
      Division division = Division.of(searchKey);

      // 검색어 입력 시
      if (Objects.nonNull(searchValue)) {
        form.setPageResult(categoryRepository.findAllByDivisionAndNameContaining(division, searchValue, pageable));
      }
      // 검색어 미입력 시
      else {
        form.setPageResult(categoryRepository.findAllByDivision(division, pageable));
      }
    }
  }

  /**
   * 카테고리 등록
   * - 입력값 (분류값, name)이 없는 경우,
   *   해당 분류에 이미 등록된 카테고리가 있는 경우,
   *   해당 분류의 최대 분류코드값이 999인 경우 실패 응답
   * - 분류코드 범위 : 101000000 ~ 999999999
   *   - 대분류 : 앞자리 세글자 (101~999) 외 나머지 0
   *   - 중분류 : 중간 세글자 (001~999) 외 나머지 0
   *   - 소분류 : 끝 세글자 (001~999)
   */
  @Override
  @Transactional
  public Category createCategory(AdminCategoryForm form) {
    Division division = Division.of(form.getDivision());
    int divisionMax = categoryRepository.findDivisionMax(form.getDivision());

    // 분류 코드 유효성 파악
    validateCreateCategoryFormData(form, division, divisionMax);

    // 새로운 분류코드
    String newCode = createDivisionCode(division, divisionMax, form);

    // 카테고리 저장
    return categoryRepository.save(
        Category.builder()
            .division(division)
            .code(newCode)
            .name(form.getName())
            .build()
    );
  }

  private void validateCreateCategoryFormData(AdminCategoryForm form, Division division, int divisionMax) {

    if (Objects.isNull(form.getName())) {
      throw new ButlerCategoryException(ErrorCode.INVALID_CATEGORY_DATA);
    }

    if (Objects.isNull(form.getDivision())) {
      throw new ButlerCategoryException(ErrorCode.INVALID_CATEGORY_DATA);
    }

    if (categoryRepository.existsByDivisionAndName(division, form.getName())) {
      throw new ButlerCategoryException(ErrorCode.CATEGORY_ALREADY_EXIST);
    }

    if (divisionMax == 999) {
      throw new ButlerCategoryException(ErrorCode.CATEGORY_DIVISION_FULL);
    }

  }

  private String createDivisionCode(Division division, int divisionMax, AdminCategoryForm form) {

    // 부분 division code : int -> String
    int numerator = divisionMax + 1;
    int denominator = 100;
    StringBuilder sb = new StringBuilder();

    for (int i = 0; i < 3; i++) {

      sb.append(numerator / denominator);

      numerator = numerator % denominator;

      denominator = denominator / 10;
    }

    // 전체 division code
    if (MAIN.equals(division)) {
      // 대분류일 때
      return String.format("%s000000", sb);

    } else if (MEDIUM.equals(division)) {
      // 중분류일 때
      String mainCode = extractDivisionCode(MAIN, form.getMain());

      return String.format("%s%s000", mainCode, sb);

    } else{
      // 소분류일 때
      String mediumCode = extractDivisionCode(MEDIUM, form.getMedium());

      return String.format("%s%s", mediumCode, sb);
    }

  }

  public String extractDivisionCode(Division division, String name) {

    String categoryCode = findByDivisionAndName(division, name).getCode();

    int len = (division.ordinal() + 1) * 3;

    return categoryCode.substring(0, len);

  }

  public Category findByDivisionAndName(Division division, String name) {
    return categoryRepository.findByDivisionAndName(division, name)
        .orElseThrow(() -> new ButlerCategoryException(ErrorCode.CATEGORY_NOT_FOUND));
  }

  /**
   * 카테고리 수정
   * - 분류 코드와 일치하는 카테고리가 없는 경우,
   *   해당 분류에 새로운 카테고리명과 일치하는 카테고리명이 있는 경우 실패 응답
   */
  @Override
  @Transactional
  public Category updateCategoryName(AdminCategoryForm form) {

    String code = form.getCode();
    String name = form.getName();

    Category category = categoryRepository.findByCode(code)
        .orElseThrow(() -> new ButlerCategoryException(ErrorCode.CATEGORY_NOT_FOUND));

    if (categoryRepository.existsByDivisionAndName(Division.fromCode(code), name)) {
      throw new ButlerCategoryException(ErrorCode.CATEGORY_ALREADY_EXIST);
    }

    category.setName(name);

    return category;
  }

  /**
   * 카테고리 삭제
   * - 해당 분류 코드 카테고리가 없는 경우,
   *   연결된 상품이 한 개 이상 있는 경우, 실패 응답
   */
  @Override
  @Transactional
  public void deleteCategory(String code) {

    Category category = categoryRepository.findByCode(code)
        .orElseThrow(() -> new ButlerCategoryException(ErrorCode.CATEGORY_NOT_FOUND));

    categoryRepository.delete(category);

  }

}
