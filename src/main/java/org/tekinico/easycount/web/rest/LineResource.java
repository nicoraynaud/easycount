package org.tekinico.easycount.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tekinico.easycount.service.LineService;
import org.tekinico.easycount.service.dto.LineDTO;
import org.tekinico.easycount.web.rest.util.HeaderUtil;
import org.tekinico.easycount.web.rest.util.PaginationUtil;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Line.
 */
@RestController
@RequestMapping("/api")
public class LineResource {

    private final Logger log = LoggerFactory.getLogger(LineResource.class);

    private static final String ENTITY_NAME = "line";

    private final LineService lineService;

    public LineResource(LineService lineService) {
        this.lineService = lineService;
    }

    /**
     * POST  /lines : Create a new line.
     *
     * @param lineDTO the lineDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new lineDTO, or with status 400 (Bad Request) if the line has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/lines")
    @Timed
    public ResponseEntity<LineDTO> createLine(@Valid @RequestBody LineDTO lineDTO) throws URISyntaxException {
        log.debug("REST request to save Line : {}", lineDTO);
        if (lineDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new line cannot already have an ID")).body(null);
        }
        LineDTO result = lineService.save(lineDTO);
        return ResponseEntity.created(new URI("/api/lines/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /lines : Updates an existing line.
     *
     * @param lineDTO the lineDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated lineDTO,
     * or with status 400 (Bad Request) if the lineDTO is not valid,
     * or with status 500 (Internal Server Error) if the lineDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/lines")
    @Timed
    public ResponseEntity<LineDTO> updateLine(@Valid @RequestBody LineDTO lineDTO) throws URISyntaxException {
        log.debug("REST request to update Line : {}", lineDTO);
        if (lineDTO.getId() == null) {
            return createLine(lineDTO);
        }
        LineDTO result = lineService.save(lineDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, lineDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /lines : get all the lines.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of lines in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/lines")
    @Timed
    public ResponseEntity<List<LineDTO>> getAllLines(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Lines");
        Page<LineDTO> page = lineService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/lines");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /lines/:id : get the "id" line.
     *
     * @param id the id of the lineDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the lineDTO, or with status 404 (Not Found)
     */
    @GetMapping("/lines/{id}")
    @Timed
    public ResponseEntity<LineDTO> getLine(@PathVariable Long id) {
        log.debug("REST request to get Line : {}", id);
        LineDTO lineDTO = lineService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(lineDTO));
    }

    /**
     * DELETE  /lines/:id : delete the "id" line.
     *
     * @param id the id of the lineDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/lines/{id}")
    @Timed
    public ResponseEntity<Void> deleteLine(@PathVariable Long id) {
        log.debug("REST request to delete Line : {}", id);
        lineService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * GET  /bankAccount/bankAccountId/lines : get all the lines of a given bankAccount.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of lines in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/bank-accounts/{bankAccountId}/lines")
    @Timed
    public ResponseEntity<List<LineDTO>> getAllLines(@PathVariable Long bankAccountId, @ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Lines for a given bankAccount");
        Page<LineDTO> page = lineService.findAllByBankAccount(bankAccountId, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/bank-accounts/" + bankAccountId + "/lines");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
}
