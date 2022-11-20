package com.example.petbutler.admin.model;

import com.example.petbutler.persist.entity.AnimalHosptl;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminAnimalHosptlApiForm {

  // - 검색 필터용
  private String searchKey;
  private String searchValue;

  // - 검색 결과
  private Page<AnimalHosptl> pageResult;

}
