package org.tekinico.easycount.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.tekinico.easycount.domain.BankAccount;
import org.tekinico.easycount.domain.Category;
import org.tekinico.easycount.domain.enumeration.LineSource;
import org.tekinico.easycount.domain.enumeration.LineStatus;
import org.tekinico.easycount.repository.CategoryRepository;
import org.tekinico.easycount.service.LineService;
import org.tekinico.easycount.domain.Line;
import org.tekinico.easycount.repository.LineRepository;
import org.tekinico.easycount.service.dto.LineDTO;
import org.tekinico.easycount.service.exceptions.ImportException;
import org.tekinico.easycount.service.mapper.LineMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.tekinico.easycount.service.util.CSVUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Line.
 */
@Service
@Transactional
public class LineServiceImpl implements LineService{

    private final Logger log = LoggerFactory.getLogger(LineServiceImpl.class);

    private final LineRepository lineRepository;

    private final LineMapper lineMapper;

    private final CategoryRepository categoryRepository;

    public LineServiceImpl(LineRepository lineRepository, LineMapper lineMapper, CategoryRepository categoryRepository) {
        this.lineRepository = lineRepository;
        this.lineMapper = lineMapper;
        this.categoryRepository = categoryRepository;
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
     * Get all the lines for a given bank account.
     *
     * @param bankAccountId the bankAccount id
     * @param pageable      the pagination information
     * @return the list of entities
     */
    @Override
    public Page<LineDTO> findAllByBankAccount(Long bankAccountId, Pageable pageable) {
        log.debug("Request to get all Lines of a given bankAccount : {}", bankAccountId);
        Page<Line> result = lineRepository.findByBankAccountId(pageable, bankAccountId);
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
    }

    /**
     * Import a list of lines in a CSV file
     *
     * @param file the CSV file
     * @param bankAccount the BankAccount to lin them to
     * @return the list of lines that have been imported
     */
    @Override
    public List<Line> importLines(MultipartFile file, BankAccount bankAccount) {

        List<Line> parsedLines = parseLines(file);

        parsedLines.forEach(l -> l.setBankAccount(bankAccount));

        lineRepository.save(parsedLines);

        return parsedLines;
    }

    /**
     * Parses de CSV file in order to generate lines objects
     * This method is also responsible for deducing the category linked to that line
     *
     * @param file the CSV file
     * @return the list of lines that have been parsed
     */
    public List<Line> parseLines(MultipartFile file) {

        if (file == null)
            return new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            return reader.lines()
                .skip(1)
                .map(line -> parseCsvLineToLine(CSVUtils.parseLine(line)))
                .collect(Collectors.toList());
        } catch (IOException e) {
            throw new ImportException("An error occured while importing lines", e);
        }
    }

    /**
     * Format string array to a line
     * [0] - Some id             (do not use)
     * [1] - Date                (2017-02-09)
     * [2] - Effective date      (2017-02-09 06:11:01)
     * [3] - Creation date       (2017-02-09 06:11:01)
     * [4] - label / description (string)
     * [5] - Credit              (202543.0 as string)
     * [6] - Debit               (202543.0 as string)
     * [7] - Source              (Manuel|Automatique)
     * [8] - Status              (Nouvelle|Rapprochée|Annulé)
     * [9] - Category            (string)
     *
     * This method creates new Categories when they do not already exist.
     *
     * @param properties The list of properties extracted from the csv line
     * @return a Line with properties correctly set
     */
    protected Line parseCsvLineToLine(List<String> properties) {

        Line line = new Line();
        // Date -- 1
        line.setDate(LocalDate.parse(properties.get(1)));
        // dont use properties.get(2)
        // dont use properties.get(3)
        // Label-- 4
        line.setLabel(properties.get(4));
        // Credit -- 5
        if (StringUtils.isNotBlank(properties.get(5))) {
            line.setCredit(Double.parseDouble(properties.get(5)));
        }
        // Debit -- 6
        if (StringUtils.isNotBlank(properties.get(6))) {
            line.setDebit(Double.parseDouble(properties.get(6)));
        }
        // Source -- 7
        line.setSource(properties.get(7).equals("Manuel") ? LineSource.MANUAL : LineSource.GENERATED);
        // Status -- 8
        line.setStatus(
            properties.get(8).equals("Nouvelle") ? LineStatus.NEW :
                properties.get(8).equals("Rapprochée") ? LineStatus.TICKED :
                    LineStatus.CANCELLED);
        // Category -- 9
        String category = properties.get(9);
        if (StringUtils.isNotBlank(category)) {
            Category cat = categoryRepository.findFirstByLabelLike(category);
            if (cat == null && StringUtils.isNotBlank(category)) {
                cat = new Category();
                cat.setLabel(category);
                categoryRepository.save(cat);
            }
            line.getCategories().add(cat);
        }

        return line;
    }
}
