package com.example.petbutler.repository;

import com.example.petbutler.domain.user.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
  Optional<User> findByEmailAuthKey(String emailAuthKey);
}
