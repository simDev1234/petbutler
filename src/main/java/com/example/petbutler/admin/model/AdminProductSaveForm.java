package com.example.petbutler.admin.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AdminProductSaveForm {

  // 상품 pk, 동물 병원 pk (동물병원 관리에서 사용)
  private Long productId;
  private Long hosptlId;

  // 카테고리 분류
  private String main;
  private String medium;
  private String small;

  // 이미지
  private MultipartFile thumbnail;

  // 제품 정보
  private String brand;    // 제품 브랜드
  private String name;     // 제품 이름

}
