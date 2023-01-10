package com.example.petbutler.admin.service;

import com.example.petbutler.admin.model.AdminAnimalHosptlApiForm;
import com.example.petbutler.persist.entity.AnimalHosptl;
import com.example.petbutler.persist.entity.Product;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminAnimalHosptlService {

  Page<AnimalHosptl> getAnimalHosptlList(AdminAnimalHosptlApiForm form, Pageable pageable);

  void addProductToAnimalHosptl(Long hosptlId, Product product);

  String getProductsFromHospl(Long id);
}
