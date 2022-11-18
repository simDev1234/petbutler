package com.example.petbutler.persist;

import com.example.petbutler.persist.entity.User;
import com.example.petbutler.model.constants.UserRole;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  boolean existsByEmail(String email);

  Optional<User> findByEmailAndEmailAuthKey(String email, String emailAuthKey);

  Optional<User> findByEmail(String email);

  Page<User> findAllByEmailContainsIgnoreCase(String email, Pageable pageable);

  Page<User> findAllByUserRolesContainingAndEmailContainsIgnoreCase(UserRole userRole, String email, Pageable pageable);

  Page<User> findAll(Pageable pageable);
}
