package org.tekinico.easycount.service.impl;

import org.tekinico.easycount.service.LineTemplateService;
import org.tekinico.easycount.domain.LineTemplate;
import org.tekinico.easycount.repository.LineTemplateRepository;
import org.tekinico.easycount.repository.search.LineTemplateSearchRepository;
import org.tekinico.easycount.service.dto.LineTemplateDTO;
import org.tekinico.easycount.service.mapper.LineTemplateMapper;
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
 * Service Implementation for managing LineTemplate.
 */
@Service
@Transactional
public class LineTemplateServiceImpl implements LineTemplateService{

    private final Logger log = LoggerFactory.getLogger(LineTemplateServiceImpl.class);
    
    private final LineTemplateRepository lineTemplateRepository;

    private final LineTemplateMapper lineTemplateMapper;

    private final LineTemplateSearchRepository lineTemplateSearchRepository;

    public LineTemplateServiceImpl(LineTemplateRepository lineTemplateRepository, LineTemplateMapper lineTemplateMapper, LineTemplateSearchRepository lineTemplateSearchRepository) {
        this.lineTemplateRepository = lineTemplateRepository;
        this.lineTemplateMapper = lineTemplateMapper;
        this.lineTemplateSearchRepository = lineTemplateSearchRepository;
    }

    /**
     * Save a lineTemplate.
     *
     * @param lineTemplateDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public LineTemplateDTO save(LineTemplateDTO lineTemplateDTO) {
        log.debug("Request to save LineTemplate : {}", lineTemplateDTO);
        LineTemplate lineTemplate = lineTemplateMapper.lineTemplateDTOToLineTemplate(lineTemplateDTO);
        lineTemplate = lineTemplateRepository.save(lineTemplate);
        LineTemplateDTO result = lineTemplateMapper.lineTemplateToLineTemplateDTO(lineTemplate);
        lineTemplateSearchRepository.save(lineTemplate);
        return result;
    }

    /**
     *  Get all the lineTemplates.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<LineTemplateDTO> findAll(Pageable pageable) {
        log.debug("Request to get all LineTemplates");
        Page<LineTemplate> result = lineTemplateRepository.findAll(pageable);
        return result.map(lineTemplate -> lineTemplateMapper.lineTemplateToLineTemplateDTO(lineTemplate));
    }

    /**
     *  Get one lineTemplate by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public LineTemplateDTO findOne(Long id) {
        log.debug("Request to get LineTemplate : {}", id);
        LineTemplate lineTemplate = lineTemplateRepository.findOneWithEagerRelationships(id);
        LineTemplateDTO lineTemplateDTO = lineTemplateMapper.lineTemplateToLineTemplateDTO(lineTemplate);
        return lineTemplateDTO;
    }

    /**
     *  Delete the  lineTemplate by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete LineTemplate : {}", id);
        lineTemplateRepository.delete(id);
        lineTemplateSearchRepository.delete(id);
    }

    /**
     * Search for the lineTemplate corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<LineTemplateDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of LineTemplates for query {}", query);
        Page<LineTemplate> result = lineTemplateSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(lineTemplate -> lineTemplateMapper.lineTemplateToLineTemplateDTO(lineTemplate));
    }
}
