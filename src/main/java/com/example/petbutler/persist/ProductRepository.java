package com.example.petbutler.persist;

import com.example.petbutler.persist.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

  @Query(value = "select "
                + "    if(count(*) > 0, 'true', 'false') "
                + "from product p "
                + "where substr(p.category_code, 1, ?2) = substr(?1, 1, ?2) "
                + "and   p.stock > 0 "
                + "limit 1 "
        , nativeQuery = true)
  boolean existsAllByCategoryCodeAndStockGreaterThan(String code, int len);

  @Query(value = "select count(*) from product p where substr(p.category_code, 1, ?2) = substr(?1, 1, ?2)"
        , nativeQuery = true)
  int countProductByCategoryCode(String code, int len);

}
