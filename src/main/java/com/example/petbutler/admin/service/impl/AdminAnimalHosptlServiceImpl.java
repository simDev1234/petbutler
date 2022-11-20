package com.example.petbutler.admin.service.impl;

import com.example.petbutler.admin.model.AdminAnimalHosptlApiForm;
import com.example.petbutler.admin.service.AdminAnimalHosptlService;
import com.example.petbutler.exception.ButlerApiException;
import com.example.petbutler.exception.constants.ErrorCode;
import com.example.petbutler.persist.AnimalHosptlRepository;
import com.example.petbutler.persist.entity.AnimalHosptl;
import com.example.petbutler.persist.entity.Product;
import io.netty.util.internal.StringUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
@RequiredArgsConstructor
public class AdminAnimalHosptlServiceImpl implements AdminAnimalHosptlService {

  private final AnimalHosptlRepository animalHosptlRepository;

  /**
   * 동물병원 조회
   */
  @Override
  public Page<AnimalHosptl> getAnimalHosptlList(AdminAnimalHosptlApiForm form, Pageable pageable) {

    // 검색 조건이 있을 때
    if (!StringUtil.isNullOrEmpty(form.getSearchValue())) {

      // 시군구 기준 검색
      if ("sigun".equals(form.getSearchKey())) {
        return animalHosptlRepository.findAllBySigunContaining(pageable, form.getSearchValue());
      }
      // 병원명 기준 검색
      else if ("hosptlName".equals(form.getSearchKey())) {
        return animalHosptlRepository.findAllByHosptlNameContaining(pageable, form.getSearchValue());
      }

    }

    // 검색 조건이 없을 때
    return animalHosptlRepository.findAll(pageable);
  }

  /**
   * 동물병원에 상품 등록하기
   */
  @Override
  @Transactional
  public void addProductToAnimalHosptl(long hosptlId, Product product) {

    AnimalHosptl animalHosptl = animalHosptlRepository.findById(hosptlId)
        .orElseThrow(() -> new ButlerApiException(ErrorCode.API_DATA_HOSPITAL_NOT_FOUND));

    List<Product> products = animalHosptl.getProducts();

    if (CollectionUtils.isEmpty(products)) {
      products = new ArrayList();
    }

    products.add(product);

    animalHosptl.setProducts(products);

  }

  /**
   * 동물병원 보유상품 보기
   *
   * @return
   */
  @Override
  public List<Product> getProductsFromHospl(long id) {

    AnimalHosptl animalHosptl = animalHosptlRepository.findById(id)
        .orElseThrow(() -> new ButlerApiException(ErrorCode.API_DATA_HOSPITAL_NOT_FOUND));

    if (Objects.isNull(animalHosptl.getProducts())) {
      new ButlerApiException(ErrorCode.API_DATA_HOSPITAL_HAS_NO_PRODUCTS);
    }

    return animalHosptl.getProducts();
  }
}
