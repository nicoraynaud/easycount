package org.tekinico.easycount.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.tekinico.easycount.domain.Currency;

import org.tekinico.easycount.repository.CurrencyRepository;
import org.tekinico.easycount.repository.search.CurrencySearchRepository;
import org.tekinico.easycount.web.rest.util.HeaderUtil;
import org.tekinico.easycount.web.rest.util.PaginationUtil;
import org.tekinico.easycount.service.dto.CurrencyDTO;
import org.tekinico.easycount.service.mapper.CurrencyMapper;
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
 * REST controller for managing Currency.
 */
@RestController
@RequestMapping("/api")
public class CurrencyResource {

    private final Logger log = LoggerFactory.getLogger(CurrencyResource.class);

    private static final String ENTITY_NAME = "currency";
        
    private final CurrencyRepository currencyRepository;

    private final CurrencyMapper currencyMapper;

    private final CurrencySearchRepository currencySearchRepository;

    public CurrencyResource(CurrencyRepository currencyRepository, CurrencyMapper currencyMapper, CurrencySearchRepository currencySearchRepository) {
        this.currencyRepository = currencyRepository;
        this.currencyMapper = currencyMapper;
        this.currencySearchRepository = currencySearchRepository;
    }

    /**
     * POST  /currencies : Create a new currency.
     *
     * @param currencyDTO the currencyDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new currencyDTO, or with status 400 (Bad Request) if the currency has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/currencies")
    @Timed
    public ResponseEntity<CurrencyDTO> createCurrency(@Valid @RequestBody CurrencyDTO currencyDTO) throws URISyntaxException {
        log.debug("REST request to save Currency : {}", currencyDTO);
        if (currencyDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new currency cannot already have an ID")).body(null);
        }
        Currency currency = currencyMapper.currencyDTOToCurrency(currencyDTO);
        currency = currencyRepository.save(currency);
        CurrencyDTO result = currencyMapper.currencyToCurrencyDTO(currency);
        currencySearchRepository.save(currency);
        return ResponseEntity.created(new URI("/api/currencies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /currencies : Updates an existing currency.
     *
     * @param currencyDTO the currencyDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated currencyDTO,
     * or with status 400 (Bad Request) if the currencyDTO is not valid,
     * or with status 500 (Internal Server Error) if the currencyDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/currencies")
    @Timed
    public ResponseEntity<CurrencyDTO> updateCurrency(@Valid @RequestBody CurrencyDTO currencyDTO) throws URISyntaxException {
        log.debug("REST request to update Currency : {}", currencyDTO);
        if (currencyDTO.getId() == null) {
            return createCurrency(currencyDTO);
        }
        Currency currency = currencyMapper.currencyDTOToCurrency(currencyDTO);
        currency = currencyRepository.save(currency);
        CurrencyDTO result = currencyMapper.currencyToCurrencyDTO(currency);
        currencySearchRepository.save(currency);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, currencyDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /currencies : get all the currencies.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of currencies in body
     */
    @GetMapping("/currencies")
    @Timed
    public ResponseEntity<List<CurrencyDTO>> getAllCurrencies(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Currencies");
        Page<Currency> page = currencyRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/currencies");
        return new ResponseEntity<>(currencyMapper.currenciesToCurrencyDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /currencies/:id : get the "id" currency.
     *
     * @param id the id of the currencyDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the currencyDTO, or with status 404 (Not Found)
     */
    @GetMapping("/currencies/{id}")
    @Timed
    public ResponseEntity<CurrencyDTO> getCurrency(@PathVariable Long id) {
        log.debug("REST request to get Currency : {}", id);
        Currency currency = currencyRepository.findOne(id);
        CurrencyDTO currencyDTO = currencyMapper.currencyToCurrencyDTO(currency);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(currencyDTO));
    }

    /**
     * DELETE  /currencies/:id : delete the "id" currency.
     *
     * @param id the id of the currencyDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/currencies/{id}")
    @Timed
    public ResponseEntity<Void> deleteCurrency(@PathVariable Long id) {
        log.debug("REST request to delete Currency : {}", id);
        currencyRepository.delete(id);
        currencySearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/currencies?query=:query : search for the currency corresponding
     * to the query.
     *
     * @param query the query of the currency search 
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/currencies")
    @Timed
    public ResponseEntity<List<CurrencyDTO>> searchCurrencies(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Currencies for query {}", query);
        Page<Currency> page = currencySearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/currencies");
        return new ResponseEntity<>(currencyMapper.currenciesToCurrencyDTOs(page.getContent()), headers, HttpStatus.OK);
    }


}
