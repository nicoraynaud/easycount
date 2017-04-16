package org.tekinico.easycount.repository;

import org.tekinico.easycount.domain.LineTemplate;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the LineTemplate entity.
 */
@SuppressWarnings("unused")
public interface LineTemplateRepository extends JpaRepository<LineTemplate,Long> {

    @Query("select distinct lineTemplate from LineTemplate lineTemplate left join fetch lineTemplate.categories")
    List<LineTemplate> findAllWithEagerRelationships();

    @Query("select lineTemplate from LineTemplate lineTemplate left join fetch lineTemplate.categories where lineTemplate.id =:id")
    LineTemplate findOneWithEagerRelationships(@Param("id") Long id);

}
