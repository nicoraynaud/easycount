package org.tekinico.easycount.web.rest;

import org.tekinico.easycount.EasycountApp;

import org.tekinico.easycount.domain.LineTemplate;
import org.tekinico.easycount.repository.LineTemplateRepository;
import org.tekinico.easycount.service.LineTemplateService;
import org.tekinico.easycount.repository.search.LineTemplateSearchRepository;
import org.tekinico.easycount.service.dto.LineTemplateDTO;
import org.tekinico.easycount.service.mapper.LineTemplateMapper;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the LineTemplateResource REST controller.
 *
 * @see LineTemplateResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = EasycountApp.class)
public class LineTemplateResourceIntTest {

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_LABEL = "BBBBBBBBBB";

    private static final Double DEFAULT_DEBIT = 1D;
    private static final Double UPDATED_DEBIT = 2D;

    private static final Double DEFAULT_CREDIT = 1D;
    private static final Double UPDATED_CREDIT = 2D;

    private static final Double DEFAULT_BALANCE = 1D;
    private static final Double UPDATED_BALANCE = 2D;

    @Autowired
    private LineTemplateRepository lineTemplateRepository;

    @Autowired
    private LineTemplateMapper lineTemplateMapper;

    @Autowired
    private LineTemplateService lineTemplateService;

    @Autowired
    private LineTemplateSearchRepository lineTemplateSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restLineTemplateMockMvc;

    private LineTemplate lineTemplate;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        LineTemplateResource lineTemplateResource = new LineTemplateResource(lineTemplateService);
        this.restLineTemplateMockMvc = MockMvcBuilders.standaloneSetup(lineTemplateResource)
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
    public static LineTemplate createEntity(EntityManager em) {
        LineTemplate lineTemplate = new LineTemplate()
            .date(DEFAULT_DATE)
            .label(DEFAULT_LABEL)
            .debit(DEFAULT_DEBIT)
            .credit(DEFAULT_CREDIT)
            .balance(DEFAULT_BALANCE);
        return lineTemplate;
    }

    @Before
    public void initTest() {
        lineTemplateSearchRepository.deleteAll();
        lineTemplate = createEntity(em);
    }

    @Test
    @Transactional
    public void createLineTemplate() throws Exception {
        int databaseSizeBeforeCreate = lineTemplateRepository.findAll().size();

        // Create the LineTemplate
        LineTemplateDTO lineTemplateDTO = lineTemplateMapper.lineTemplateToLineTemplateDTO(lineTemplate);
        restLineTemplateMockMvc.perform(post("/api/line-templates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(lineTemplateDTO)))
            .andExpect(status().isCreated());

        // Validate the LineTemplate in the database
        List<LineTemplate> lineTemplateList = lineTemplateRepository.findAll();
        assertThat(lineTemplateList).hasSize(databaseSizeBeforeCreate + 1);
        LineTemplate testLineTemplate = lineTemplateList.get(lineTemplateList.size() - 1);
        assertThat(testLineTemplate.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testLineTemplate.getLabel()).isEqualTo(DEFAULT_LABEL);
        assertThat(testLineTemplate.getDebit()).isEqualTo(DEFAULT_DEBIT);
        assertThat(testLineTemplate.getCredit()).isEqualTo(DEFAULT_CREDIT);
        assertThat(testLineTemplate.getBalance()).isEqualTo(DEFAULT_BALANCE);

        // Validate the LineTemplate in Elasticsearch
        LineTemplate lineTemplateEs = lineTemplateSearchRepository.findOne(testLineTemplate.getId());
        assertThat(lineTemplateEs).isEqualToComparingFieldByField(testLineTemplate);
    }

    @Test
    @Transactional
    public void createLineTemplateWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = lineTemplateRepository.findAll().size();

        // Create the LineTemplate with an existing ID
        lineTemplate.setId(1L);
        LineTemplateDTO lineTemplateDTO = lineTemplateMapper.lineTemplateToLineTemplateDTO(lineTemplate);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLineTemplateMockMvc.perform(post("/api/line-templates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(lineTemplateDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<LineTemplate> lineTemplateList = lineTemplateRepository.findAll();
        assertThat(lineTemplateList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = lineTemplateRepository.findAll().size();
        // set the field null
        lineTemplate.setDate(null);

        // Create the LineTemplate, which fails.
        LineTemplateDTO lineTemplateDTO = lineTemplateMapper.lineTemplateToLineTemplateDTO(lineTemplate);

        restLineTemplateMockMvc.perform(post("/api/line-templates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(lineTemplateDTO)))
            .andExpect(status().isBadRequest());

        List<LineTemplate> lineTemplateList = lineTemplateRepository.findAll();
        assertThat(lineTemplateList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLabelIsRequired() throws Exception {
        int databaseSizeBeforeTest = lineTemplateRepository.findAll().size();
        // set the field null
        lineTemplate.setLabel(null);

        // Create the LineTemplate, which fails.
        LineTemplateDTO lineTemplateDTO = lineTemplateMapper.lineTemplateToLineTemplateDTO(lineTemplate);

        restLineTemplateMockMvc.perform(post("/api/line-templates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(lineTemplateDTO)))
            .andExpect(status().isBadRequest());

        List<LineTemplate> lineTemplateList = lineTemplateRepository.findAll();
        assertThat(lineTemplateList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLineTemplates() throws Exception {
        // Initialize the database
        lineTemplateRepository.saveAndFlush(lineTemplate);

        // Get all the lineTemplateList
        restLineTemplateMockMvc.perform(get("/api/line-templates?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lineTemplate.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL.toString())))
            .andExpect(jsonPath("$.[*].debit").value(hasItem(DEFAULT_DEBIT.doubleValue())))
            .andExpect(jsonPath("$.[*].credit").value(hasItem(DEFAULT_CREDIT.doubleValue())))
            .andExpect(jsonPath("$.[*].balance").value(hasItem(DEFAULT_BALANCE.doubleValue())));
    }

    @Test
    @Transactional
    public void getLineTemplate() throws Exception {
        // Initialize the database
        lineTemplateRepository.saveAndFlush(lineTemplate);

        // Get the lineTemplate
        restLineTemplateMockMvc.perform(get("/api/line-templates/{id}", lineTemplate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(lineTemplate.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.label").value(DEFAULT_LABEL.toString()))
            .andExpect(jsonPath("$.debit").value(DEFAULT_DEBIT.doubleValue()))
            .andExpect(jsonPath("$.credit").value(DEFAULT_CREDIT.doubleValue()))
            .andExpect(jsonPath("$.balance").value(DEFAULT_BALANCE.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingLineTemplate() throws Exception {
        // Get the lineTemplate
        restLineTemplateMockMvc.perform(get("/api/line-templates/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLineTemplate() throws Exception {
        // Initialize the database
        lineTemplateRepository.saveAndFlush(lineTemplate);
        lineTemplateSearchRepository.save(lineTemplate);
        int databaseSizeBeforeUpdate = lineTemplateRepository.findAll().size();

        // Update the lineTemplate
        LineTemplate updatedLineTemplate = lineTemplateRepository.findOne(lineTemplate.getId());
        updatedLineTemplate
            .date(UPDATED_DATE)
            .label(UPDATED_LABEL)
            .debit(UPDATED_DEBIT)
            .credit(UPDATED_CREDIT)
            .balance(UPDATED_BALANCE);
        LineTemplateDTO lineTemplateDTO = lineTemplateMapper.lineTemplateToLineTemplateDTO(updatedLineTemplate);

        restLineTemplateMockMvc.perform(put("/api/line-templates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(lineTemplateDTO)))
            .andExpect(status().isOk());

        // Validate the LineTemplate in the database
        List<LineTemplate> lineTemplateList = lineTemplateRepository.findAll();
        assertThat(lineTemplateList).hasSize(databaseSizeBeforeUpdate);
        LineTemplate testLineTemplate = lineTemplateList.get(lineTemplateList.size() - 1);
        assertThat(testLineTemplate.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testLineTemplate.getLabel()).isEqualTo(UPDATED_LABEL);
        assertThat(testLineTemplate.getDebit()).isEqualTo(UPDATED_DEBIT);
        assertThat(testLineTemplate.getCredit()).isEqualTo(UPDATED_CREDIT);
        assertThat(testLineTemplate.getBalance()).isEqualTo(UPDATED_BALANCE);

        // Validate the LineTemplate in Elasticsearch
        LineTemplate lineTemplateEs = lineTemplateSearchRepository.findOne(testLineTemplate.getId());
        assertThat(lineTemplateEs).isEqualToComparingFieldByField(testLineTemplate);
    }

    @Test
    @Transactional
    public void updateNonExistingLineTemplate() throws Exception {
        int databaseSizeBeforeUpdate = lineTemplateRepository.findAll().size();

        // Create the LineTemplate
        LineTemplateDTO lineTemplateDTO = lineTemplateMapper.lineTemplateToLineTemplateDTO(lineTemplate);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restLineTemplateMockMvc.perform(put("/api/line-templates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(lineTemplateDTO)))
            .andExpect(status().isCreated());

        // Validate the LineTemplate in the database
        List<LineTemplate> lineTemplateList = lineTemplateRepository.findAll();
        assertThat(lineTemplateList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteLineTemplate() throws Exception {
        // Initialize the database
        lineTemplateRepository.saveAndFlush(lineTemplate);
        lineTemplateSearchRepository.save(lineTemplate);
        int databaseSizeBeforeDelete = lineTemplateRepository.findAll().size();

        // Get the lineTemplate
        restLineTemplateMockMvc.perform(delete("/api/line-templates/{id}", lineTemplate.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean lineTemplateExistsInEs = lineTemplateSearchRepository.exists(lineTemplate.getId());
        assertThat(lineTemplateExistsInEs).isFalse();

        // Validate the database is empty
        List<LineTemplate> lineTemplateList = lineTemplateRepository.findAll();
        assertThat(lineTemplateList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchLineTemplate() throws Exception {
        // Initialize the database
        lineTemplateRepository.saveAndFlush(lineTemplate);
        lineTemplateSearchRepository.save(lineTemplate);

        // Search the lineTemplate
        restLineTemplateMockMvc.perform(get("/api/_search/line-templates?query=id:" + lineTemplate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lineTemplate.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL.toString())))
            .andExpect(jsonPath("$.[*].debit").value(hasItem(DEFAULT_DEBIT.doubleValue())))
            .andExpect(jsonPath("$.[*].credit").value(hasItem(DEFAULT_CREDIT.doubleValue())))
            .andExpect(jsonPath("$.[*].balance").value(hasItem(DEFAULT_BALANCE.doubleValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LineTemplate.class);
    }
}
