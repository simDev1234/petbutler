package com.example.petbutler.repository;

import com.example.petbutler.entity.Seller;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SellerRepository extends JpaRepository<Seller, Long> {

  Optional<Seller> findByEmail(String email);

  Optional<Seller> findByEmailAuthKey(String emailAuthKey);
}
