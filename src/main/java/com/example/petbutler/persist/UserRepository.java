package com.example.petbutler.persist;

import com.example.petbutler.persist.entity.User;
import com.example.petbutler.type.UserRole;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByEmailAndEmailAuthKey(String email, String emailAuthKey);

  Optional<User> findByEmail(String email);

  Page<User> findAllByEmailContainsIgnoreCase(String email, Pageable pageable);

  Page<User> findAllByUserRoleAndEmailContainsIgnoreCase(UserRole userRole, String email, Pageable pageable);

  Page<User> findAll(Pageable pageable);
}
