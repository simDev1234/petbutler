package com.example.petbutler.model;

import com.example.petbutler.persist.entity.Pet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class PetDetail {

  private String thumbnailUrlPath;

  private String kind;

  private String name;

  public static PetDetail from(Pet pet) {
    return PetDetail.builder()
        .thumbnailUrlPath(pet.getThumbnailUrlPath())
        .kind(pet.getKind())
        .name(pet.getName())
        .build();
  }
}
