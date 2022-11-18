package com.example.petbutler.persist;

import com.example.petbutler.persist.entity.AnimalHosptl;
import com.example.petbutler.persist.entity.Pet;
import com.example.petbutler.persist.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnimalHosptlRepository extends JpaRepository<AnimalHosptl, Long> {

  boolean existsByAddress(String address);

}
