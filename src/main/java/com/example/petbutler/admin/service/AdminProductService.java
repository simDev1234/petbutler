package com.example.petbutler.admin.service;

import com.example.petbutler.admin.model.AdminProductSearchForm;
import com.example.petbutler.persist.entity.Product;
import org.springframework.data.domain.Pageable;

public interface AdminProductService {

  AdminProductSearchForm getProductList(AdminProductSearchForm form, Pageable pageable);

  Product findProductById(long productId);

}
