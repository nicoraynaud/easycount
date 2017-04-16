package org.tekinico.easycount.service.impl;

import org.joda.time.Months;
import org.tekinico.easycount.domain.Line;
import org.tekinico.easycount.domain.enumeration.LineSource;
import org.tekinico.easycount.domain.enumeration.LineStatus;
import org.tekinico.easycount.repository.LineRepository;
import org.tekinico.easycount.service.LineService;
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

import java.time.LocalDate;
import java.time.ZonedDateTime;
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
public class LineTemplateServiceImpl implements LineTemplateService {

    private final Logger log = LoggerFactory.getLogger(LineTemplateServiceImpl.class);

    private final LineTemplateRepository lineTemplateRepository;

    private final LineTemplateMapper lineTemplateMapper;

    private final LineTemplateSearchRepository lineTemplateSearchRepository;

    private final LineRepository lineRepository;

    public LineTemplateServiceImpl(
        LineTemplateRepository lineTemplateRepository,
        LineTemplateMapper lineTemplateMapper,
        LineTemplateSearchRepository lineTemplateSearchRepository,
        LineRepository lineRepository) {
        this.lineTemplateRepository = lineTemplateRepository;
        this.lineTemplateMapper = lineTemplateMapper;
        this.lineTemplateSearchRepository = lineTemplateSearchRepository;
        this.lineRepository = lineRepository;
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
     * Get all the lineTemplates.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<LineTemplateDTO> findAll(Pageable pageable) {
        log.debug("Request to get all LineTemplates");
        Page<LineTemplate> result = lineTemplateRepository.findAll(pageable);
        return result.map(lineTemplate -> lineTemplateMapper.lineTemplateToLineTemplateDTO(lineTemplate));
    }

    /**
     * Get one lineTemplate by id.
     *
     * @param id the id of the entity
     * @return the entity
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
     * Delete the  lineTemplate by id.
     *
     * @param id the id of the entity
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
     * @param query    the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<LineTemplateDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of LineTemplates for query {}", query);
        Page<LineTemplate> result = lineTemplateSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(lineTemplate -> lineTemplateMapper.lineTemplateToLineTemplateDTO(lineTemplate));
    }

    /**
     * Generate new Lines according to current active LineTemplates
     *
     * @param bankAccountId The Bank account to generate the lines into
     * @param asOf the date as of to start the generation
     */
    @Override
    public void generateNewLinesForMonth(Long bankAccountId, LocalDate asOf) {

        List<LineTemplate> lineTemplates = lineTemplateRepository.findAllForGeneration(bankAccountId);

        for (LineTemplate tpl : lineTemplates) {

            // If date is past, d'ont generate
            if (tpl.getDayOfMonth() < asOf.getDayOfMonth()) {
                continue;
            }

            // if template has already been used for this month, d'ont generate
            LocalDate startDate = asOf.withDayOfMonth(1);
            LocalDate endDate = asOf.withDayOfMonth(asOf.lengthOfMonth());
            if (lineRepository.existsByTemplateAndDateGreaterThanEqualAndDateLessThanEqual(tpl, startDate, endDate)) {
                continue;
            }

            Line line = new Line();
            line.setDate(LocalDate.of(asOf.getYear(), asOf.getMonth(), tpl.getDayOfMonth()));
            line.setTemplate(tpl);
            line.setStatus(LineStatus.NEW);
            line.setSource(LineSource.GENERATED);
            line.setDebit(tpl.getDebit());
            line.setCredit(tpl.getCredit());
            line.setLabel(tpl.getLabel());
            line.setBankAccount(tpl.getBankAccount());
            line.setCreateDate(ZonedDateTime.now());
            line.getCategories().addAll(tpl.getCategories());

            lineRepository.save(line);
        }
    }
}
