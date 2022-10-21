package com.example.petbutler.type;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PetType {

  CAT("고양이"),
  DOG("강아지");

  private final String description;

}
