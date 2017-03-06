package org.tekinico.easycount.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.tekinico.easycount.domain.Line;

import java.util.List;

/**
 * Spring Data JPA repository for the Line entity.
 */
@SuppressWarnings("unused")
public interface LineRepository extends JpaRepository<Line,Long> {

    @Query("select distinct line from Line line left join fetch line.categories")
    List<Line> findAllWithEagerRelationships();

    @Query("select line from Line line left join fetch line.categories where line.id =:id")
    Line findOneWithEagerRelationships(@Param("id") Long id);

    Page<Line> findByBankAccountId(Pageable page, Long bankAccountId);

}
