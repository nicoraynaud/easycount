package org.tekinico.easycount.web.rest;

import org.tekinico.easycount.EasycountApp;

import org.tekinico.easycount.domain.Bank;
import org.tekinico.easycount.repository.BankRepository;
import org.tekinico.easycount.repository.search.BankSearchRepository;
import org.tekinico.easycount.service.dto.BankDTO;
import org.tekinico.easycount.service.mapper.BankMapper;
import org.tekinico.easycount.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the BankResource REST controller.
 *
 * @see BankResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = EasycountApp.class)
public class BankResourceIntTest {

    private static final String DEFAULT_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_LABEL = "BBBBBBBBBB";

    @Autowired
    private BankRepository bankRepository;

    @Autowired
    private BankMapper bankMapper;

    @Autowired
    private BankSearchRepository bankSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restBankMockMvc;

    private Bank bank;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BankResource bankResource = new BankResource(bankRepository, bankMapper, bankSearchRepository);
        this.restBankMockMvc = MockMvcBuilders.standaloneSetup(bankResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Bank createEntity(EntityManager em) {
        Bank bank = new Bank()
            .label(DEFAULT_LABEL);
        return bank;
    }

    @Before
    public void initTest() {
        bankSearchRepository.deleteAll();
        bank = createEntity(em);
    }

    @Test
    @Transactional
    public void createBank() throws Exception {
        int databaseSizeBeforeCreate = bankRepository.findAll().size();

        // Create the Bank
        BankDTO bankDTO = bankMapper.bankToBankDTO(bank);
        restBankMockMvc.perform(post("/api/banks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bankDTO)))
            .andExpect(status().isCreated());

        // Validate the Bank in the database
        List<Bank> bankList = bankRepository.findAll();
        assertThat(bankList).hasSize(databaseSizeBeforeCreate + 1);
        Bank testBank = bankList.get(bankList.size() - 1);
        assertThat(testBank.getLabel()).isEqualTo(DEFAULT_LABEL);

        // Validate the Bank in Elasticsearch
        Bank bankEs = bankSearchRepository.findOne(testBank.getId());
        assertThat(bankEs).isEqualToComparingFieldByField(testBank);
    }

    @Test
    @Transactional
    public void createBankWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = bankRepository.findAll().size();

        // Create the Bank with an existing ID
        bank.setId(1L);
        BankDTO bankDTO = bankMapper.bankToBankDTO(bank);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBankMockMvc.perform(post("/api/banks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bankDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Bank> bankList = bankRepository.findAll();
        assertThat(bankList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkLabelIsRequired() throws Exception {
        int databaseSizeBeforeTest = bankRepository.findAll().size();
        // set the field null
        bank.setLabel(null);

        // Create the Bank, which fails.
        BankDTO bankDTO = bankMapper.bankToBankDTO(bank);

        restBankMockMvc.perform(post("/api/banks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bankDTO)))
            .andExpect(status().isBadRequest());

        List<Bank> bankList = bankRepository.findAll();
        assertThat(bankList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBanks() throws Exception {
        // Initialize the database
        bankRepository.saveAndFlush(bank);

        // Get all the bankList
        restBankMockMvc.perform(get("/api/banks?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bank.getId().intValue())))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL.toString())));
    }

    @Test
    @Transactional
    public void getBank() throws Exception {
        // Initialize the database
        bankRepository.saveAndFlush(bank);

        // Get the bank
        restBankMockMvc.perform(get("/api/banks/{id}", bank.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(bank.getId().intValue()))
            .andExpect(jsonPath("$.label").value(DEFAULT_LABEL.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingBank() throws Exception {
        // Get the bank
        restBankMockMvc.perform(get("/api/banks/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBank() throws Exception {
        // Initialize the database
        bankRepository.saveAndFlush(bank);
        bankSearchRepository.save(bank);
        int databaseSizeBeforeUpdate = bankRepository.findAll().size();

        // Update the bank
        Bank updatedBank = bankRepository.findOne(bank.getId());
        updatedBank
            .label(UPDATED_LABEL);
        BankDTO bankDTO = bankMapper.bankToBankDTO(updatedBank);

        restBankMockMvc.perform(put("/api/banks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bankDTO)))
            .andExpect(status().isOk());

        // Validate the Bank in the database
        List<Bank> bankList = bankRepository.findAll();
        assertThat(bankList).hasSize(databaseSizeBeforeUpdate);
        Bank testBank = bankList.get(bankList.size() - 1);
        assertThat(testBank.getLabel()).isEqualTo(UPDATED_LABEL);

        // Validate the Bank in Elasticsearch
        Bank bankEs = bankSearchRepository.findOne(testBank.getId());
        assertThat(bankEs).isEqualToComparingFieldByField(testBank);
    }

    @Test
    @Transactional
    public void updateNonExistingBank() throws Exception {
        int databaseSizeBeforeUpdate = bankRepository.findAll().size();

        // Create the Bank
        BankDTO bankDTO = bankMapper.bankToBankDTO(bank);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restBankMockMvc.perform(put("/api/banks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bankDTO)))
            .andExpect(status().isCreated());

        // Validate the Bank in the database
        List<Bank> bankList = bankRepository.findAll();
        assertThat(bankList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteBank() throws Exception {
        // Initialize the database
        bankRepository.saveAndFlush(bank);
        bankSearchRepository.save(bank);
        int databaseSizeBeforeDelete = bankRepository.findAll().size();

        // Get the bank
        restBankMockMvc.perform(delete("/api/banks/{id}", bank.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean bankExistsInEs = bankSearchRepository.exists(bank.getId());
        assertThat(bankExistsInEs).isFalse();

        // Validate the database is empty
        List<Bank> bankList = bankRepository.findAll();
        assertThat(bankList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchBank() throws Exception {
        // Initialize the database
        bankRepository.saveAndFlush(bank);
        bankSearchRepository.save(bank);

        // Search the bank
        restBankMockMvc.perform(get("/api/_search/banks?query=id:" + bank.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bank.getId().intValue())))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Bank.class);
    }
}
