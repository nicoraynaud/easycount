package org.tekinico.easycount.web.rest;

import org.tekinico.easycount.EasycountApp;

import org.tekinico.easycount.domain.Line;
import org.tekinico.easycount.repository.LineRepository;
import org.tekinico.easycount.service.LineService;
import org.tekinico.easycount.repository.search.LineSearchRepository;
import org.tekinico.easycount.service.dto.LineDTO;
import org.tekinico.easycount.service.mapper.LineMapper;
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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static org.tekinico.easycount.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.tekinico.easycount.domain.enumeration.LineStatus;
import org.tekinico.easycount.domain.enumeration.LineSource;
/**
 * Test class for the LineResource REST controller.
 *
 * @see LineResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = EasycountApp.class)
public class LineResourceIntTest {

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

    private static final LineStatus DEFAULT_STATUS = LineStatus.NEW;
    private static final LineStatus UPDATED_STATUS = LineStatus.TICKED;

    private static final LineSource DEFAULT_SOURCE = LineSource.MANUAL;
    private static final LineSource UPDATED_SOURCE = LineSource.GENERATED;

    private static final ZonedDateTime DEFAULT_CREATE_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATE_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private LineRepository lineRepository;

    @Autowired
    private LineMapper lineMapper;

    @Autowired
    private LineService lineService;

    @Autowired
    private LineSearchRepository lineSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restLineMockMvc;

    private Line line;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        LineResource lineResource = new LineResource(lineService);
        this.restLineMockMvc = MockMvcBuilders.standaloneSetup(lineResource)
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
    public static Line createEntity(EntityManager em) {
        Line line = new Line()
            .date(DEFAULT_DATE)
            .label(DEFAULT_LABEL)
            .debit(DEFAULT_DEBIT)
            .credit(DEFAULT_CREDIT)
            .balance(DEFAULT_BALANCE)
            .status(DEFAULT_STATUS)
            .source(DEFAULT_SOURCE)
            .createDate(DEFAULT_CREATE_DATE);
        return line;
    }

    @Before
    public void initTest() {
        lineSearchRepository.deleteAll();
        line = createEntity(em);
    }

    @Test
    @Transactional
    public void createLine() throws Exception {
        int databaseSizeBeforeCreate = lineRepository.findAll().size();

        // Create the Line
        LineDTO lineDTO = lineMapper.lineToLineDTO(line);
        restLineMockMvc.perform(post("/api/lines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(lineDTO)))
            .andExpect(status().isCreated());

        // Validate the Line in the database
        List<Line> lineList = lineRepository.findAll();
        assertThat(lineList).hasSize(databaseSizeBeforeCreate + 1);
        Line testLine = lineList.get(lineList.size() - 1);
        assertThat(testLine.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testLine.getLabel()).isEqualTo(DEFAULT_LABEL);
        assertThat(testLine.getDebit()).isEqualTo(DEFAULT_DEBIT);
        assertThat(testLine.getCredit()).isEqualTo(DEFAULT_CREDIT);
        assertThat(testLine.getBalance()).isEqualTo(DEFAULT_BALANCE);
        assertThat(testLine.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testLine.getSource()).isEqualTo(DEFAULT_SOURCE);
        assertThat(testLine.getCreateDate().getDayOfMonth()).isEqualTo(ZonedDateTime.now(ZoneId.systemDefault()).getDayOfMonth());
        assertThat(testLine.getCreateDate().getMonth()).isEqualTo(ZonedDateTime.now(ZoneId.systemDefault()).getMonth());
        assertThat(testLine.getCreateDate().getYear()).isEqualTo(ZonedDateTime.now(ZoneId.systemDefault()).getYear());

        // Validate the Line in Elasticsearch
        Line lineEs = lineSearchRepository.findOne(testLine.getId());
        //assertThat(lineEs).isEqualToComparingFieldByField(testLine);
    }

    @Test
    @Transactional
    public void createLineWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = lineRepository.findAll().size();

        // Create the Line with an existing ID
        line.setId(1L);
        LineDTO lineDTO = lineMapper.lineToLineDTO(line);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLineMockMvc.perform(post("/api/lines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(lineDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Line> lineList = lineRepository.findAll();
        assertThat(lineList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = lineRepository.findAll().size();
        // set the field null
        line.setDate(null);

        // Create the Line, which fails.
        LineDTO lineDTO = lineMapper.lineToLineDTO(line);

        restLineMockMvc.perform(post("/api/lines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(lineDTO)))
            .andExpect(status().isBadRequest());

        List<Line> lineList = lineRepository.findAll();
        assertThat(lineList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLabelIsRequired() throws Exception {
        int databaseSizeBeforeTest = lineRepository.findAll().size();
        // set the field null
        line.setLabel(null);

        // Create the Line, which fails.
        LineDTO lineDTO = lineMapper.lineToLineDTO(line);

        restLineMockMvc.perform(post("/api/lines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(lineDTO)))
            .andExpect(status().isBadRequest());

        List<Line> lineList = lineRepository.findAll();
        assertThat(lineList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = lineRepository.findAll().size();
        // set the field null
        line.setStatus(null);

        // Create the Line, which fails.
        LineDTO lineDTO = lineMapper.lineToLineDTO(line);

        restLineMockMvc.perform(post("/api/lines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(lineDTO)))
            .andExpect(status().isBadRequest());

        List<Line> lineList = lineRepository.findAll();
        assertThat(lineList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreateDateIsSetAtCreationTime() throws Exception {
        int databaseSizeBeforeTest = lineRepository.findAll().size();
        // set the field null
        line.setCreateDate(null);

        // Create the Line, which fails.
        LineDTO lineDTO = lineMapper.lineToLineDTO(line);

        restLineMockMvc.perform(post("/api/lines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(lineDTO)))
            .andExpect(status().isCreated());

        List<Line> lineList = lineRepository.findAll();
        assertThat(lineList).hasSize(databaseSizeBeforeTest + 1);
    }

    @Test
    @Transactional
    public void getAllLines() throws Exception {
        // Initialize the database
        lineRepository.saveAndFlush(line);

        // Get all the lineList
        restLineMockMvc.perform(get("/api/lines?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(line.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL.toString())))
            .andExpect(jsonPath("$.[*].debit").value(hasItem(DEFAULT_DEBIT.doubleValue())))
            .andExpect(jsonPath("$.[*].credit").value(hasItem(DEFAULT_CREDIT.doubleValue())))
            .andExpect(jsonPath("$.[*].balance").value(hasItem(DEFAULT_BALANCE.doubleValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].source").value(hasItem(DEFAULT_SOURCE.toString())))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(sameInstant(DEFAULT_CREATE_DATE))));
    }

    @Test
    @Transactional
    public void getLine() throws Exception {
        // Initialize the database
        lineRepository.saveAndFlush(line);

        // Get the line
        restLineMockMvc.perform(get("/api/lines/{id}", line.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(line.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.label").value(DEFAULT_LABEL.toString()))
            .andExpect(jsonPath("$.debit").value(DEFAULT_DEBIT.doubleValue()))
            .andExpect(jsonPath("$.credit").value(DEFAULT_CREDIT.doubleValue()))
            .andExpect(jsonPath("$.balance").value(DEFAULT_BALANCE.doubleValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.source").value(DEFAULT_SOURCE.toString()))
            .andExpect(jsonPath("$.createDate").value(sameInstant(DEFAULT_CREATE_DATE)));
    }

    @Test
    @Transactional
    public void getNonExistingLine() throws Exception {
        // Get the line
        restLineMockMvc.perform(get("/api/lines/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLine() throws Exception {
        // Initialize the database
        lineRepository.saveAndFlush(line);
        lineSearchRepository.save(line);
        int databaseSizeBeforeUpdate = lineRepository.findAll().size();

        // Update the line
        Line updatedLine = lineRepository.findOne(line.getId());
        updatedLine
            .date(UPDATED_DATE)
            .label(UPDATED_LABEL)
            .debit(UPDATED_DEBIT)
            .credit(UPDATED_CREDIT)
            .balance(UPDATED_BALANCE)
            .status(UPDATED_STATUS)
            .source(UPDATED_SOURCE)
            .createDate(UPDATED_CREATE_DATE);
        LineDTO lineDTO = lineMapper.lineToLineDTO(updatedLine);

        restLineMockMvc.perform(put("/api/lines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(lineDTO)))
            .andExpect(status().isOk());

        // Validate the Line in the database
        List<Line> lineList = lineRepository.findAll();
        assertThat(lineList).hasSize(databaseSizeBeforeUpdate);
        Line testLine = lineList.get(lineList.size() - 1);
        assertThat(testLine.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testLine.getLabel()).isEqualTo(UPDATED_LABEL);
        assertThat(testLine.getDebit()).isEqualTo(UPDATED_DEBIT);
        assertThat(testLine.getCredit()).isEqualTo(UPDATED_CREDIT);
        assertThat(testLine.getBalance()).isEqualTo(UPDATED_BALANCE);
        assertThat(testLine.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testLine.getSource()).isEqualTo(UPDATED_SOURCE);
        assertThat(testLine.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);

        // Validate the Line in Elasticsearch
        Line lineEs = lineSearchRepository.findOne(testLine.getId());
        assertThat(lineEs).isEqualToComparingFieldByField(testLine);
    }

    @Test
    @Transactional
    public void updateNonExistingLine() throws Exception {
        int databaseSizeBeforeUpdate = lineRepository.findAll().size();

        // Create the Line
        LineDTO lineDTO = lineMapper.lineToLineDTO(line);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restLineMockMvc.perform(put("/api/lines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(lineDTO)))
            .andExpect(status().isCreated());

        // Validate the Line in the database
        List<Line> lineList = lineRepository.findAll();
        assertThat(lineList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteLine() throws Exception {
        // Initialize the database
        lineRepository.saveAndFlush(line);
        lineSearchRepository.save(line);
        int databaseSizeBeforeDelete = lineRepository.findAll().size();

        // Get the line
        restLineMockMvc.perform(delete("/api/lines/{id}", line.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean lineExistsInEs = lineSearchRepository.exists(line.getId());
        assertThat(lineExistsInEs).isFalse();

        // Validate the database is empty
        List<Line> lineList = lineRepository.findAll();
        assertThat(lineList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchLine() throws Exception {
        // Initialize the database
        lineRepository.saveAndFlush(line);
        lineSearchRepository.save(line);

        // Search the line
        restLineMockMvc.perform(get("/api/_search/lines?query=id:" + line.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(line.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL.toString())))
            .andExpect(jsonPath("$.[*].debit").value(hasItem(DEFAULT_DEBIT.doubleValue())))
            .andExpect(jsonPath("$.[*].credit").value(hasItem(DEFAULT_CREDIT.doubleValue())))
            .andExpect(jsonPath("$.[*].balance").value(hasItem(DEFAULT_BALANCE.doubleValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].source").value(hasItem(DEFAULT_SOURCE.toString())))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(sameInstant(DEFAULT_CREATE_DATE))));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Line.class);
    }
}
