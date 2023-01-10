package com.example.petbutler.service;

import com.example.petbutler.model.AnimalHosptlApiForm;
import com.example.petbutler.persist.entity.AnimalHosptl;
import java.util.List;

public interface AnimalHosptlService {

  List<AnimalHosptl> getHosptlList(AnimalHosptlApiForm form);

}
