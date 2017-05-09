package org.tekinico.easycount.repository;

import org.tekinico.easycount.domain.Line;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

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

}
