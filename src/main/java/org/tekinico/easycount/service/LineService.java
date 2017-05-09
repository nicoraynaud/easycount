package org.tekinico.easycount.service;

import org.tekinico.easycount.service.dto.LineDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Service Interface for managing Line.
 */
public interface LineService {

    /**
     * Save a line.
     *
     * @param lineDTO the entity to save
     * @return the persisted entity
     */
    LineDTO save(LineDTO lineDTO);

    /**
     *  Get all the lines.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<LineDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" line.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    LineDTO findOne(Long id);

    /**
     *  Delete the "id" line.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the line corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<LineDTO> search(String query, Pageable pageable);
}
