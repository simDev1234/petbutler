package com.example.petbutler.admin.model;

import com.example.petbutler.persist.entity.Category;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AdminCategoryForm {

  // 등록시 사용
  // - 분류별 코드
  private String main;
  private String medium;
  private String small;

  // 조회, 수정, 삭제시 사용
  // - 분류명, 분류코드, 카테고리명
  private String division;
  private String code;
  private String name;

  // - 분류별 카테고리 리스트
  private List<String> mains;
  private List<String> mediums;
  private List<String> smalls;

  // - 검색 필터용
  private String searchKey;
  private String searchValue;

  // - 페이징 기능 추가 리스트
  private Page<Category> pageResult;

  // - 분류코드별 상품수
  private Map<String, Integer> productCntMap;

}
