package org.tekinico.easycount.service.impl;

import org.springframework.web.multipart.MultipartFile;
import org.tekinico.easycount.domain.Line;
import org.tekinico.easycount.repository.LineRepository;
import org.tekinico.easycount.service.BankAccountService;
import org.tekinico.easycount.domain.BankAccount;
import org.tekinico.easycount.repository.BankAccountRepository;
import org.tekinico.easycount.service.LineService;
import org.tekinico.easycount.service.dto.BankAccountDTO;
import org.tekinico.easycount.service.exceptions.ImportException;
import org.tekinico.easycount.service.mapper.BankAccountMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing BankAccount.
 */
@Service
@Transactional
public class BankAccountServiceImpl implements BankAccountService{

    private final Logger log = LoggerFactory.getLogger(BankAccountServiceImpl.class);

    private final BankAccountRepository bankAccountRepository;

    private final BankAccountMapper bankAccountMapper;

    private final LineService lineService;

    private final LineRepository lineRepository;

    public BankAccountServiceImpl(BankAccountRepository bankAccountRepository,
                                  BankAccountMapper bankAccountMapper,
                                  LineService lineService,
                                  LineRepository lineRepository) {
        this.bankAccountRepository = bankAccountRepository;
        this.bankAccountMapper = bankAccountMapper;
        this.lineService = lineService;
        this.lineRepository = lineRepository;
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

        // Fetch its current balance (as of today)
        Double balance = lineRepository.banlanceOfAccount(id, LocalDate.now());
        bankAccountDTO.setBalance(balance);

        // Fetch its ticked balance
        Double balanceTicked = lineRepository.banlanceOfAccountAndTicked(id);
        bankAccountDTO.setBalanceTicked(balanceTicked);

        // Fetch its End Of Month balance
        Double balanceEom = lineRepository.banlanceOfAccount(id, LocalDate.now().with( TemporalAdjusters.lastDayOfMonth() ));
        bankAccountDTO.setBalanceEom(balanceEom);

        // Fetch its End Of Month +1 balance
        Double balanceEomP1 = lineRepository.banlanceOfAccount(id, LocalDate.now()
            .with( TemporalAdjusters.firstDayOfNextMonth() )
            .with( TemporalAdjusters.lastDayOfMonth() ));
        bankAccountDTO.setBalanceEomP1(balanceEomP1);

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
    }

    @Override
    public void importLines(Long bankAccountId, MultipartFile file) {
        log.debug("Request to import lines into bank account : {}", bankAccountId);

        BankAccount ba = bankAccountRepository.getOne(bankAccountId);

        if (ba == null) {
            throw new ImportException("The bank account cannot be null when importing new lines");
        }

        // Parse the lines
        List<Line> linesToImport = lineService.importLines(file, ba);

        // Save all
        bankAccountRepository.saveAndFlush(ba);
    }
}
