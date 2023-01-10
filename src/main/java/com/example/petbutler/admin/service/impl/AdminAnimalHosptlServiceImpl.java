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
import java.util.stream.Collectors;
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
   * - 동물병원에 이미 해당 상품이 등록된 경우 실패 응답
   */
  @Override
  @Transactional
  public void addProductToAnimalHosptl(Long hosptlId, Product product) {

    AnimalHosptl animalHosptl = animalHosptlRepository.findById(hosptlId)
        .orElseThrow(() -> new ButlerApiException(ErrorCode.API_DATA_HOSPITAL_NOT_FOUND));

    List<Product> products = animalHosptl.getProducts();

    if (CollectionUtils.isEmpty(products)) {
      products = new ArrayList();
    }

    validateIfProductAlreadyExist(product, products);

    products.add(product);

    animalHosptl.setProducts(products);

  }

  private static void validateIfProductAlreadyExist(Product product, List<Product> products) {

    if (products.contains(product)) {
      throw new ButlerApiException(ErrorCode.API_DATA_ALREADY_EXIST);
    }

  }

  /**
   * 동물병원 보유상품 보기
   */
  @Override
  public String getProductsFromHospl(Long id) {

    AnimalHosptl animalHosptl = animalHosptlRepository.findById(id)
        .orElseThrow(() -> new ButlerApiException(ErrorCode.API_DATA_HOSPITAL_NOT_FOUND));

    List<Product> products = animalHosptl.getProducts();

    // 병원에 상품이 있는 경우
    if (!CollectionUtils.isEmpty(products)){

      // 중복 데이터 삭제(상품 등록 코드 작업 초기에 중복된 데이터 발생하였음)
      products = products.stream().distinct().collect(Collectors.toList());

      // 상품 리스트를 문자열(ex. 츄르, 개껌)로 반환
      return productsToString(products);

    }

    return "상품이 없습니다.";
  }

  private static String productsToString(List<Product> products) {

    StringBuilder sb = new StringBuilder();

    for (int i = 0; i < products.size(); i++) {
      if (i == 0) {
        sb.append(products.get(i).getName());
      } else {
        sb.append(String.format(", %s", products.get(i).getName()));
      }
    }

    return sb.toString();
  }

}
