package org.tekinico.easycount.service;

import org.tekinico.easycount.service.dto.LineTemplateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Service Interface for managing LineTemplate.
 */
public interface LineTemplateService {

    /**
     * Save a lineTemplate.
     *
     * @param lineTemplateDTO the entity to save
     * @return the persisted entity
     */
    LineTemplateDTO save(LineTemplateDTO lineTemplateDTO);

    /**
     *  Get all the lineTemplates.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<LineTemplateDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" lineTemplate.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    LineTemplateDTO findOne(Long id);

    /**
     *  Delete the "id" lineTemplate.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the lineTemplate corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<LineTemplateDTO> search(String query, Pageable pageable);
}
