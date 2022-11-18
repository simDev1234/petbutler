package com.example.petbutler.persist.entity;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
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
public class AnimalHosptl extends BaseEntity{

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // 병원 정보
  private String sigun;      // 시군명
  private String hosptlName; // 사업장명
  private String phone;      // 전화
  private String address;    // 도로명 주소
  private double lat;        // 위도
  private double lon;        // 경도

  // 보유 물품
  @OneToMany(fetch = FetchType.EAGER)
  @JoinColumn
  private List<Product> products;

}
