package org.tekinico.easycount.service.impl;

import org.tekinico.easycount.service.LineService;
import org.tekinico.easycount.domain.Line;
import org.tekinico.easycount.repository.LineRepository;
import org.tekinico.easycount.repository.search.LineSearchRepository;
import org.tekinico.easycount.service.dto.LineDTO;
import org.tekinico.easycount.service.mapper.LineMapper;
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
 * Service Implementation for managing Line.
 */
@Service
@Transactional
public class LineServiceImpl implements LineService{

    private final Logger log = LoggerFactory.getLogger(LineServiceImpl.class);
    
    private final LineRepository lineRepository;

    private final LineMapper lineMapper;

    private final LineSearchRepository lineSearchRepository;

    public LineServiceImpl(LineRepository lineRepository, LineMapper lineMapper, LineSearchRepository lineSearchRepository) {
        this.lineRepository = lineRepository;
        this.lineMapper = lineMapper;
        this.lineSearchRepository = lineSearchRepository;
    }

    /**
     * Save a line.
     *
     * @param lineDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public LineDTO save(LineDTO lineDTO) {
        log.debug("Request to save Line : {}", lineDTO);
        Line line = lineMapper.lineDTOToLine(lineDTO);
        line = lineRepository.save(line);
        LineDTO result = lineMapper.lineToLineDTO(line);
        lineSearchRepository.save(line);
        return result;
    }

    /**
     *  Get all the lines.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<LineDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Lines");
        Page<Line> result = lineRepository.findAll(pageable);
        return result.map(line -> lineMapper.lineToLineDTO(line));
    }

    /**
     *  Get one line by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public LineDTO findOne(Long id) {
        log.debug("Request to get Line : {}", id);
        Line line = lineRepository.findOneWithEagerRelationships(id);
        LineDTO lineDTO = lineMapper.lineToLineDTO(line);
        return lineDTO;
    }

    /**
     *  Delete the  line by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Line : {}", id);
        lineRepository.delete(id);
        lineSearchRepository.delete(id);
    }

    /**
     * Search for the line corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<LineDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Lines for query {}", query);
        Page<Line> result = lineSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(line -> lineMapper.lineToLineDTO(line));
    }
}
