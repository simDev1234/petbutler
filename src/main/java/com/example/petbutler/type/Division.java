package com.example.petbutler.type;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Division {

  MAIN, MEDIUM, SMALL;

  public static Division of(String division) {
    if (MAIN.name().equalsIgnoreCase(division)) {
      return MAIN;
    } else if (MEDIUM.name().equalsIgnoreCase(division)) {
      return MEDIUM;
    } else {
      return SMALL;
    }
  }

  public static Division fromCode(String code) {

    if ("000000".equals(code.substring(3))) {     // 대분류일 때
      return MAIN;
    } else if ("000".equals(code.substring(6))) { // 중분류일 때
      return MEDIUM;
    } else { // 소분류일 때
      return SMALL;
    }

  }

  public static int getDivisionLen(Division division) {

    if (MAIN.equals(division)) {     // 대분류일 때
      return 3;
    } else if (MEDIUM.equals(division)) { // 중분류일 때
      return 6;
    } else { // 소분류일 때
      return 9;
    }

  }

  public static int getDivisionLen(String code) {
    return getDivisionLen(Division.fromCode(code));
  }

}
