package com.example.petbutler.persist.entity;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.AuditOverride;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@AuditOverride(forClass = BaseEntity.class)
public class Product extends BaseEntity{

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // 카테고리 분류
  private String categoryMainCode;
  private String categoryMediumCode;
  private String categorySmallCode;

  // 이미지
  private String thumbnailLocalPath;
  private String thumbnailUrlPath;

  // 제품 정보
  private String brand;    // 제품 브랜드
  private String name;     // 제품 이름
  private String kind;     // 대상 동물 분류
  private int    price;    // 정가
  private int    discount; // 할인가
  private int    stock;    // 재고

}
