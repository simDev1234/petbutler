package com.example.petbutler.persist;

import com.example.petbutler.persist.entity.AnimalHosptl;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AnimalHosptlRepository extends JpaRepository<AnimalHosptl, Long> {

  boolean existsByAddress(String address);

  Page<AnimalHosptl> findAll(Pageable pageable);

  Page<AnimalHosptl> findAllBySigunContaining(Pageable pageable, String sigun);

  Page<AnimalHosptl> findAllByHosptlNameContaining(Pageable pageable, String hosptlName);

  @Query(value =
            "select "
          + "    t.* "
          + "from ( "
          + "         select "
          + "             w.*, "
          + "             ( "
          + "                 acos "
          + "                     ( "
          + "                        sin((?1 * PI() / 180.0)) * sin((w.lat * PI() / 180.0)) "
          + "                        + cos((?1 * PI() / 180.0)) * cos((w.lat * PI() / 180.0)) "
          + "                        * cos(((?2 - w.lon) * PI() / 180.0)) "
          + "                     ) "
          + "             ) * 180 / PI() "
          + "              * 60 * 1.1515 * 1.609344 as distance "
          + "         from animal_hosptl w "
          + "     ) t "
          + "where t.distance <= ?3 "
          + "order by t.distance "
      , nativeQuery = true)
  List<AnimalHosptl> findNearTenHosptls(double lat, double lon, double distance);

}
