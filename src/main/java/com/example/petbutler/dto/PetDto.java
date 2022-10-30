package com.example.petbutler.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PetDto {

  private String kind;

  private String name;

  public static PetDto[] of(String[] kinds, String[] names) {
    
    PetDto[] petDtos = new PetDto[names.length];

    for (int i = 0; i < names.length; i++) {
      petDtos[i] = PetDto.builder()
          .kind(kinds[i])
          .name(names[i])
          .build();
    }

    return petDtos;
  }
}
