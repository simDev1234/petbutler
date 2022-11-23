package com.example.petbutler.model;

import com.example.petbutler.persist.entity.AnimalHosptl;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnimalHosptlApiForm {

  // 현재 위치
  private double currentLat;
  private double currentLon;

  // 현재 지도 레벨
  private int level;

  // 병원 정보
  private String sigun;      // 시군명
  private String hosptlName; // 사업장명
  private String phone;      // 전화
  private String address;    // 도로명 주소
  private double lat;        // 위도
  private double lon;        // 경도

  public AnimalHosptl toEntity() {
    return AnimalHosptl.builder()
        .sigun(sigun)
        .hosptlName(hosptlName)
        .phone(phone)
        .address(address)
        .lat(lat)
        .lon(lon)
        .build();
  }
}
