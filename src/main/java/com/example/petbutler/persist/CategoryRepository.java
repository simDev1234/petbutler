package com.example.petbutler.persist;

import com.example.petbutler.persist.entity.Category;
import com.example.petbutler.model.constants.Division;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

  boolean existsByDivisionAndName(Division division, String name);

  @Query(value = "select "
                + "    case m.division "
                + "    when 'MAIN' then cast(substr(m.max, 1, 3) as unsigned) "
                + "    when 'MEDIUM' then cast(substr(m.max, 4, 3) as unsigned) "
                + "    when 'SMALL' then cast(substr(m.max, 7, 3) as unsigned) "
                + "    end as max "
                + "from ( "
                + "         select "
                + "             c.division as division, "
                + "             case "
                + "                 when c.code = null then 0 "
                + "                 else c.code "
                + "                 end as max "
                + "         from Category as c "
                + "         where c.division = ?1 "
                + "         order by c.code desc "
                + "         limit 1 "
                + "     ) as m ",
         nativeQuery = true)
  int findDivisionMax(String division);

  @Query(value = "select distinct c.name from category c where c.division = ?1", nativeQuery = true)
  List<String> findDistinctNameByDivision(String division);

  List<Category> findAllByDivision(Division division);

  Page<Category> findAll(Pageable pageable);

  Page<Category> findAllByNameContaining(String searchValue, Pageable pageable);

  Page<Category> findAllByDivision(Division division, Pageable pageable);

  Page<Category> findAllByDivisionAndNameContaining(Division division, String searchValue, Pageable pageable);

  Optional<Category> findByCode(String code);

  Optional<Category> findByDivisionAndName(Division division, String name);
}
