package com.example.petbutler.persist.entity;

import com.example.petbutler.admin.model.AdminProductSaveForm;
import com.example.petbutler.utils.wrapper.FilePath;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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

  public static Product of(AdminProductSaveForm form, FilePath filePath) {
    return Product.builder()
        .categoryMainCode(form.getMain())
        .categoryMediumCode(form.getMedium())
        .categorySmallCode(form.getSmall())
        .thumbnailLocalPath(filePath.getLocalPath())
        .thumbnailUrlPath(filePath.getUrlPath())
        .brand(form.getBrand())
        .name(form.getName())
        .build();
  }

  public void update(AdminProductSaveForm form) {
    this.setName(form.getName());
    this.setBrand(form.getBrand());
    this.setCategoryMainCode(form.getMain());
    this.setCategoryMediumCode(form.getMedium());
    this.setCategorySmallCode(form.getSmall());
  }
}
