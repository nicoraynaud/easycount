package org.tekinico.easycount.service;

import org.springframework.web.multipart.MultipartFile;
import org.tekinico.easycount.service.dto.BankAccountDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Service Interface for managing BankAccount.
 */
public interface BankAccountService {

    /**
     * Save a bankAccount.
     *
     * @param bankAccountDTO the entity to save
     * @return the persisted entity
     */
    BankAccountDTO save(BankAccountDTO bankAccountDTO);

    /**
     *  Get all the bankAccounts.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<BankAccountDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" bankAccount.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    BankAccountDTO findOne(Long id);

    /**
     *  Delete the "id" bankAccount.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Import lines as CSV to the specified bankAccount
     *
     * @param bankAccountId the id of the bank account to import lines to
     */
    void importLines(Long bankAccountId, MultipartFile file);
}
