package org.tekinico.easycount.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.tekinico.easycount.service.LineTemplateService;
import org.tekinico.easycount.web.rest.util.HeaderUtil;
import org.tekinico.easycount.web.rest.util.PaginationUtil;
import org.tekinico.easycount.service.dto.LineTemplateDTO;
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
 * REST controller for managing LineTemplate.
 */
@RestController
@RequestMapping("/api")
public class LineTemplateResource {

    private final Logger log = LoggerFactory.getLogger(LineTemplateResource.class);

    private static final String ENTITY_NAME = "lineTemplate";
        
    private final LineTemplateService lineTemplateService;

    public LineTemplateResource(LineTemplateService lineTemplateService) {
        this.lineTemplateService = lineTemplateService;
    }

    /**
     * POST  /line-templates : Create a new lineTemplate.
     *
     * @param lineTemplateDTO the lineTemplateDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new lineTemplateDTO, or with status 400 (Bad Request) if the lineTemplate has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/line-templates")
    @Timed
    public ResponseEntity<LineTemplateDTO> createLineTemplate(@Valid @RequestBody LineTemplateDTO lineTemplateDTO) throws URISyntaxException {
        log.debug("REST request to save LineTemplate : {}", lineTemplateDTO);
        if (lineTemplateDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new lineTemplate cannot already have an ID")).body(null);
        }
        LineTemplateDTO result = lineTemplateService.save(lineTemplateDTO);
        return ResponseEntity.created(new URI("/api/line-templates/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /line-templates : Updates an existing lineTemplate.
     *
     * @param lineTemplateDTO the lineTemplateDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated lineTemplateDTO,
     * or with status 400 (Bad Request) if the lineTemplateDTO is not valid,
     * or with status 500 (Internal Server Error) if the lineTemplateDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/line-templates")
    @Timed
    public ResponseEntity<LineTemplateDTO> updateLineTemplate(@Valid @RequestBody LineTemplateDTO lineTemplateDTO) throws URISyntaxException {
        log.debug("REST request to update LineTemplate : {}", lineTemplateDTO);
        if (lineTemplateDTO.getId() == null) {
            return createLineTemplate(lineTemplateDTO);
        }
        LineTemplateDTO result = lineTemplateService.save(lineTemplateDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, lineTemplateDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /line-templates : get all the lineTemplates.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of lineTemplates in body
     */
    @GetMapping("/line-templates")
    @Timed
    public ResponseEntity<List<LineTemplateDTO>> getAllLineTemplates(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of LineTemplates");
        Page<LineTemplateDTO> page = lineTemplateService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/line-templates");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /line-templates/:id : get the "id" lineTemplate.
     *
     * @param id the id of the lineTemplateDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the lineTemplateDTO, or with status 404 (Not Found)
     */
    @GetMapping("/line-templates/{id}")
    @Timed
    public ResponseEntity<LineTemplateDTO> getLineTemplate(@PathVariable Long id) {
        log.debug("REST request to get LineTemplate : {}", id);
        LineTemplateDTO lineTemplateDTO = lineTemplateService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(lineTemplateDTO));
    }

    /**
     * DELETE  /line-templates/:id : delete the "id" lineTemplate.
     *
     * @param id the id of the lineTemplateDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/line-templates/{id}")
    @Timed
    public ResponseEntity<Void> deleteLineTemplate(@PathVariable Long id) {
        log.debug("REST request to delete LineTemplate : {}", id);
        lineTemplateService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/line-templates?query=:query : search for the lineTemplate corresponding
     * to the query.
     *
     * @param query the query of the lineTemplate search 
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/line-templates")
    @Timed
    public ResponseEntity<List<LineTemplateDTO>> searchLineTemplates(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of LineTemplates for query {}", query);
        Page<LineTemplateDTO> page = lineTemplateService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/line-templates");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
