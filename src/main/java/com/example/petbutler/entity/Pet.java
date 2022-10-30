package com.example.petbutler.entity;

import com.example.petbutler.dto.PetDto;
import com.example.petbutler.utils.FilePath;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.AuditOverride;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@AuditOverride(forClass = BaseEntity.class)
public class Pet extends BaseEntity{

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn
  private Customer customer;

  private String thumbnailLocalPath;

  private String thumbnailUrlPath;

  private String kind;

  private String name;

  public static Pet[] from(Customer customer, PetDto[] petDtos, List<FilePath> filePaths) {

    Pet[] pets = new Pet[petDtos.length];

    for (int i = 0; i < petDtos.length; i++) {
      pets[i] = Pet.builder()
          .customer(customer)
          .thumbnailLocalPath(filePaths.get(i).getLocalPath())
          .thumbnailUrlPath(filePaths.get(i).getUrlPath())
          .kind(petDtos[i].getKind())
          .name(petDtos[i].getName())
          .build();
    }

    return pets;
  }
}