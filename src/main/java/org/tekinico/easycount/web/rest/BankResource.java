package org.tekinico.easycount.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.tekinico.easycount.domain.Bank;

import org.tekinico.easycount.repository.BankRepository;
import org.tekinico.easycount.repository.search.BankSearchRepository;
import org.tekinico.easycount.web.rest.util.HeaderUtil;
import org.tekinico.easycount.web.rest.util.PaginationUtil;
import org.tekinico.easycount.service.dto.BankDTO;
import org.tekinico.easycount.service.mapper.BankMapper;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Bank.
 */
@RestController
@RequestMapping("/api")
public class BankResource {

    private final Logger log = LoggerFactory.getLogger(BankResource.class);

    private static final String ENTITY_NAME = "bank";
        
    private final BankRepository bankRepository;

    private final BankMapper bankMapper;

    private final BankSearchRepository bankSearchRepository;

    public BankResource(BankRepository bankRepository, BankMapper bankMapper, BankSearchRepository bankSearchRepository) {
        this.bankRepository = bankRepository;
        this.bankMapper = bankMapper;
        this.bankSearchRepository = bankSearchRepository;
    }

    /**
     * POST  /banks : Create a new bank.
     *
     * @param bankDTO the bankDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new bankDTO, or with status 400 (Bad Request) if the bank has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/banks")
    @Timed
    public ResponseEntity<BankDTO> createBank(@Valid @RequestBody BankDTO bankDTO) throws URISyntaxException {
        log.debug("REST request to save Bank : {}", bankDTO);
        if (bankDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new bank cannot already have an ID")).body(null);
        }
        Bank bank = bankMapper.bankDTOToBank(bankDTO);
        bank = bankRepository.save(bank);
        BankDTO result = bankMapper.bankToBankDTO(bank);
        bankSearchRepository.save(bank);
        return ResponseEntity.created(new URI("/api/banks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /banks : Updates an existing bank.
     *
     * @param bankDTO the bankDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated bankDTO,
     * or with status 400 (Bad Request) if the bankDTO is not valid,
     * or with status 500 (Internal Server Error) if the bankDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/banks")
    @Timed
    public ResponseEntity<BankDTO> updateBank(@Valid @RequestBody BankDTO bankDTO) throws URISyntaxException {
        log.debug("REST request to update Bank : {}", bankDTO);
        if (bankDTO.getId() == null) {
            return createBank(bankDTO);
        }
        Bank bank = bankMapper.bankDTOToBank(bankDTO);
        bank = bankRepository.save(bank);
        BankDTO result = bankMapper.bankToBankDTO(bank);
        bankSearchRepository.save(bank);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, bankDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /banks : get all the banks.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of banks in body
     */
    @GetMapping("/banks")
    @Timed
    public ResponseEntity<List<BankDTO>> getAllBanks(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Banks");
        Page<Bank> page = bankRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/banks");
        return new ResponseEntity<>(bankMapper.banksToBankDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /banks/:id : get the "id" bank.
     *
     * @param id the id of the bankDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the bankDTO, or with status 404 (Not Found)
     */
    @GetMapping("/banks/{id}")
    @Timed
    public ResponseEntity<BankDTO> getBank(@PathVariable Long id) {
        log.debug("REST request to get Bank : {}", id);
        Bank bank = bankRepository.findOne(id);
        BankDTO bankDTO = bankMapper.bankToBankDTO(bank);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(bankDTO));
    }

    /**
     * DELETE  /banks/:id : delete the "id" bank.
     *
     * @param id the id of the bankDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/banks/{id}")
    @Timed
    public ResponseEntity<Void> deleteBank(@PathVariable Long id) {
        log.debug("REST request to delete Bank : {}", id);
        bankRepository.delete(id);
        bankSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/banks?query=:query : search for the bank corresponding
     * to the query.
     *
     * @param query the query of the bank search 
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/banks")
    @Timed
    public ResponseEntity<List<BankDTO>> searchBanks(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Banks for query {}", query);
        Page<Bank> page = bankSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/banks");
        return new ResponseEntity<>(bankMapper.banksToBankDTOs(page.getContent()), headers, HttpStatus.OK);
    }


}
