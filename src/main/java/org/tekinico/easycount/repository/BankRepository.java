package org.tekinico.easycount.repository;

import org.tekinico.easycount.domain.Bank;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Bank entity.
 */
@SuppressWarnings("unused")
public interface BankRepository extends JpaRepository<Bank,Long> {

}
