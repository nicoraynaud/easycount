package org.tekinico.easycount.service;

import org.springframework.web.multipart.MultipartFile;
import org.tekinico.easycount.domain.BankAccount;
import org.tekinico.easycount.domain.Line;
import org.tekinico.easycount.service.dto.LineDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.InputStream;
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
     *  Get all the lines for a given bank account.
     *
     *  @param bankAccountId the bankAccount id
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<LineDTO> findAllByBankAccount(Long bankAccountId, Pageable pageable);

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
     * Import a list of lines in a CSV file
     *
     * @param file the CSV file
     * @param bankAccount the BankAccount to lin them to
     * @return the list of lines that have been imported
     */
    List<Line> importLines(MultipartFile file, BankAccount bankAccount);

    /**
     * Parses de CSV file in order to generate lines objects
     * This method is also responsible for deducing the category linked to that line
     *
     * @param file the CSV file
     * @return the list of lines that have been parsed
     */
    List<Line> parseLines(MultipartFile file);
}
