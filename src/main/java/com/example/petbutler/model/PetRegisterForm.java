package com.example.petbutler.model;

import com.example.petbutler.persist.entity.User;
import com.example.petbutler.persist.entity.Pet;
import com.example.petbutler.utils.FilePath;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PetRegisterForm {

  private MultipartFile[] thumbnail;

  private String[] kind;

  private String[] name;

  public List<Pet> toPets(User user, List<FilePath> newFilePaths) {

    List<Pet> pets = new ArrayList();

    for (int i = 0; i < this.name.length; i++) {

      pets.add(Pet.builder()
                  .user(user)
                  .kind(this.kind[i])
                  .name(this.kind[i])
                  .thumbnailLocalPath(newFilePaths.get(i).getLocalPath())
                  .thumbnailUrlPath(newFilePaths.get(i).getUrlPath())
                  .build());

    }

    return pets;
  }


}
