package org.tekinico.easycount.repository;

import org.tekinico.easycount.domain.Currency;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Currency entity.
 */
@SuppressWarnings("unused")
public interface CurrencyRepository extends JpaRepository<Currency,Long> {

}
