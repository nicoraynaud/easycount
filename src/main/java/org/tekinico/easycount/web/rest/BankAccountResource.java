package org.tekinico.easycount.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.springframework.web.multipart.MultipartFile;
import org.tekinico.easycount.service.BankAccountService;
import org.tekinico.easycount.web.rest.util.HeaderUtil;
import org.tekinico.easycount.web.rest.util.PaginationUtil;
import org.tekinico.easycount.service.dto.BankAccountDTO;
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

/**
 * REST controller for managing BankAccount.
 */
@RestController
@RequestMapping("/api")
public class BankAccountResource {

    private final Logger log = LoggerFactory.getLogger(BankAccountResource.class);

    private static final String ENTITY_NAME = "bankAccount";

    private final BankAccountService bankAccountService;

    public BankAccountResource(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    /**
     * POST  /bank-accounts : Create a new bankAccount.
     *
     * @param bankAccountDTO the bankAccountDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new bankAccountDTO, or with status 400 (Bad Request) if the bankAccount has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/bank-accounts")
    @Timed
    public ResponseEntity<BankAccountDTO> createBankAccount(@Valid @RequestBody BankAccountDTO bankAccountDTO) throws URISyntaxException {
        log.debug("REST request to save BankAccount : {}", bankAccountDTO);
        if (bankAccountDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new bankAccount cannot already have an ID")).body(null);
        }
        BankAccountDTO result = bankAccountService.save(bankAccountDTO);
        return ResponseEntity.created(new URI("/api/bank-accounts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /bank-accounts : Updates an existing bankAccount.
     *
     * @param bankAccountDTO the bankAccountDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated bankAccountDTO,
     * or with status 400 (Bad Request) if the bankAccountDTO is not valid,
     * or with status 500 (Internal Server Error) if the bankAccountDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/bank-accounts")
    @Timed
    public ResponseEntity<BankAccountDTO> updateBankAccount(@Valid @RequestBody BankAccountDTO bankAccountDTO) throws URISyntaxException {
        log.debug("REST request to update BankAccount : {}", bankAccountDTO);
        if (bankAccountDTO.getId() == null) {
            return createBankAccount(bankAccountDTO);
        }
        BankAccountDTO result = bankAccountService.save(bankAccountDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, bankAccountDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /bank-accounts : get all the bankAccounts.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of bankAccounts in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/bank-accounts")
    @Timed
    public ResponseEntity<List<BankAccountDTO>> getAllBankAccounts(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of BankAccounts");
        Page<BankAccountDTO> page = bankAccountService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/bank-accounts");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /bank-accounts/:id : get the "id" bankAccount.
     *
     * @param id the id of the bankAccountDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the bankAccountDTO, or with status 404 (Not Found)
     */
    @GetMapping("/bank-accounts/{id}")
    @Timed
    public ResponseEntity<BankAccountDTO> getBankAccount(@PathVariable Long id) {
        log.debug("REST request to get BankAccount : {}", id);
        BankAccountDTO bankAccountDTO = bankAccountService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(bankAccountDTO));
    }

    /**
     * DELETE  /bank-accounts/:id : delete the "id" bankAccount.
     *
     * @param id the id of the bankAccountDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/bank-accounts/{id}")
    @Timed
    public ResponseEntity<Void> deleteBankAccount(@PathVariable Long id) {
        log.debug("REST request to delete BankAccount : {}", id);
        bankAccountService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * DELETE  /bank-accounts/:id : delete the "id" bankAccount.
     *
     * @param id the id of the bankAccountDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @PostMapping("/bank-accounts/{id}/import-lines")
    @Timed
    public ResponseEntity<Void> importLines(@PathVariable Long id,@RequestParam("file") MultipartFile file) {
        log.debug("REST request to import lines to BankAccount {} : file {}", id, file);
        bankAccountService.importLines(id, file);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, id.toString())).build();
    }
}
