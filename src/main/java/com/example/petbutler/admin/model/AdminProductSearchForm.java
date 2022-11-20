package com.example.petbutler.admin.model;

import com.example.petbutler.persist.entity.Category;
import com.example.petbutler.persist.entity.Product;
import com.example.petbutler.model.constants.Division;
import java.util.List;
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
public class AdminProductSearchForm {

  private List<Category> mains;
  private List<Category> mediums;
  private List<Category> smalls;

  private String main;
  private String medium;
  private String small;

  private String searchKey;
  private String searchValue;

  private Page<Product> pageResult;

  private String hosptlName;

  public Division getDivision(){

    // 아무 분류도 선택하지 않은 경우
    if ((main == null && medium == null && small == null) ||
        (main.equals("none") && medium.equals("none") && small.equals("none"))) {
      return null;
    }
    // 대분류까지 검색
    else if (!main.equals("none") && medium.equals("none") && small.equals("none")) {
      return Division.MAIN;
    }
    // 중분류까지 검색
    else if (!main.equals("none") && !medium.equals("none") && small.equals("none")) {
      return Division.MEDIUM;
    }
    // 소분류까지 검색
    else if (!main.equals("none") && !medium.equals("none") && !small.equals("none")) {
      return Division.SMALL;
    }

    return null;
  }

  public String getCategoryCode(){

    Division division = getDivision();

    // 대분류만 검색
    if (Division.MAIN.equals(division)) {
      return main;
    }
    // 중분류까지 검색
    else if (Division.MEDIUM.equals(division)) {
      return medium;
    }
    // 소분류까지 검색
    else if (Division.SMALL.equals(division)){
      return small;
    }
    // 아무 분류도 선택하지 않은 경우
    else {
      return null;
    }
  }

}
