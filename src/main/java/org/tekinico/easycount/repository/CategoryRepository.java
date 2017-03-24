package org.tekinico.easycount.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tekinico.easycount.domain.Category;

import java.util.Optional;

/**
 * Spring Data JPA repository for the Category entity.
 */
@SuppressWarnings("unused")
public interface CategoryRepository extends JpaRepository<Category,Long> {

    Category findFirstByLabelLike(String label);
}
