package com.example.petbutler.repository;

import com.example.petbutler.entity.Customer;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
  Optional<Customer> findByEmailAuthKey(String emailAuthKey);

  Optional<Customer> findByEmail(String email);
}
