package com.example.petbutler.type;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum CategoryLevel {
  LARGE_CATEGORY("대분류"),
  MEDIUM_CATEGORY("중분류"),
  SMALL_CATEGORY("소분류");

  private final String description;
}
