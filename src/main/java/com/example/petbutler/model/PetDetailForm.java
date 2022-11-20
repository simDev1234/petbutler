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
public class PetDetailForm {

  private long id;

  private String thumbnailUrlPath;

  private String kind;

  private String name;

  public static PetDetailForm from(Pet pet) {
    return PetDetailForm.builder()
        .id(pet.getId())
        .thumbnailUrlPath(pet.getThumbnailUrlPath())
        .kind(pet.getKind())
        .name(pet.getName())
        .build();
  }
}
