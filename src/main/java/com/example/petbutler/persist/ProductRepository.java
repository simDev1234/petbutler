package com.example.petbutler.persist;

import com.example.petbutler.persist.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

  int countAllByCategoryMainCode(String categoryMainCode);
  int countAllByCategoryMediumCode(String categoryMediumCode);
  int countAllByCategorySmallCode(String categorySmallCode);

  Page<Product> findAllByCategoryMainCodeAndBrandContaining(String category, String brand, Pageable pageable);
  Page<Product> findAllByCategoryMediumCodeAndBrandContaining(String category, String brand, Pageable pageable);
  Page<Product> findAllByCategorySmallCodeAndBrandContaining(String category, String brand, Pageable pageable);

  Page<Product> findAllByCategoryMainCode(String main, Pageable pageable);
  Page<Product> findAllByCategoryMediumCode(String medium, Pageable pageable);
  Page<Product> findAllByCategorySmallCode(String small, Pageable pageable);

  Page<Product> findAllByCategoryMainCodeAndNameContaining(String main, String name, Pageable pageable);

  Page<Product> findAllByCategoryMediumCodeAndNameContaining(String medium, String name, Pageable pageable);

  Page<Product> findAllByCategorySmallCodeAndNameContaining(String small, String name, Pageable pageable);

  Page<Product> findAllByBrandContaining(String brand, Pageable pageable);
  Page<Product> findAllByNameContaining(String name, Pageable pageable);
}
