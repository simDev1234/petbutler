package com.example.petbutler.persist;

import com.example.petbutler.persist.entity.Pet;
import com.example.petbutler.persist.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {

  List<Pet> findAllByUser(User user);

  boolean existsById(Long id);

}
