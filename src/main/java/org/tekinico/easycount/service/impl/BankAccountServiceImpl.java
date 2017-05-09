package org.tekinico.easycount.service.impl;

import org.tekinico.easycount.service.BankAccountService;
import org.tekinico.easycount.domain.BankAccount;
import org.tekinico.easycount.repository.BankAccountRepository;
import org.tekinico.easycount.repository.search.BankAccountSearchRepository;
import org.tekinico.easycount.service.dto.BankAccountDTO;
import org.tekinico.easycount.service.mapper.BankAccountMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing BankAccount.
 */
@Service
@Transactional
public class BankAccountServiceImpl implements BankAccountService{

    private final Logger log = LoggerFactory.getLogger(BankAccountServiceImpl.class);
    
    private final BankAccountRepository bankAccountRepository;

    private final BankAccountMapper bankAccountMapper;

    private final BankAccountSearchRepository bankAccountSearchRepository;

    public BankAccountServiceImpl(BankAccountRepository bankAccountRepository, BankAccountMapper bankAccountMapper, BankAccountSearchRepository bankAccountSearchRepository) {
        this.bankAccountRepository = bankAccountRepository;
        this.bankAccountMapper = bankAccountMapper;
        this.bankAccountSearchRepository = bankAccountSearchRepository;
    }

    /**
     * Save a bankAccount.
     *
     * @param bankAccountDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public BankAccountDTO save(BankAccountDTO bankAccountDTO) {
        log.debug("Request to save BankAccount : {}", bankAccountDTO);
        BankAccount bankAccount = bankAccountMapper.bankAccountDTOToBankAccount(bankAccountDTO);
        bankAccount = bankAccountRepository.save(bankAccount);
        BankAccountDTO result = bankAccountMapper.bankAccountToBankAccountDTO(bankAccount);
        bankAccountSearchRepository.save(bankAccount);
        return result;
    }

    /**
     *  Get all the bankAccounts.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<BankAccountDTO> findAll(Pageable pageable) {
        log.debug("Request to get all BankAccounts");
        Page<BankAccount> result = bankAccountRepository.findAll(pageable);
        return result.map(bankAccount -> bankAccountMapper.bankAccountToBankAccountDTO(bankAccount));
    }

    /**
     *  Get one bankAccount by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public BankAccountDTO findOne(Long id) {
        log.debug("Request to get BankAccount : {}", id);
        BankAccount bankAccount = bankAccountRepository.findOne(id);
        BankAccountDTO bankAccountDTO = bankAccountMapper.bankAccountToBankAccountDTO(bankAccount);
        return bankAccountDTO;
    }

    /**
     *  Delete the  bankAccount by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete BankAccount : {}", id);
        bankAccountRepository.delete(id);
        bankAccountSearchRepository.delete(id);
    }

    /**
     * Search for the bankAccount corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<BankAccountDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of BankAccounts for query {}", query);
        Page<BankAccount> result = bankAccountSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(bankAccount -> bankAccountMapper.bankAccountToBankAccountDTO(bankAccount));
    }
}
