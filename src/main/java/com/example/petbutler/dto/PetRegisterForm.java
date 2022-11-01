package com.example.petbutler.dto;

import com.example.petbutler.entity.Customer;
import com.example.petbutler.entity.Pet;
import com.example.petbutler.utils.FilePath;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PetRegisterForm {

  private MultipartFile[] thumbnail;

  private String[] kind;

  private String[] name;

  public List<Pet> toPets(Customer customer, List<FilePath> newFilePaths) {

    List<Pet> pets = new ArrayList();

    for (int i = 0; i < this.name.length; i++) {

      pets.add(Pet.builder()
                  .customer(customer)
                  .kind(this.kind[i])
                  .name(this.kind[i])
                  .thumbnailLocalPath(newFilePaths.get(i).getLocalPath())
                  .thumbnailUrlPath(newFilePaths.get(i).getUrlPath())
                  .build());

    }

    return pets;
  }


}
