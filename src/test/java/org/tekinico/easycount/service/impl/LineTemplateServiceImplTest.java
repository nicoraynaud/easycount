package org.tekinico.easycount.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.transaction.annotation.Transactional;
import org.tekinico.easycount.domain.BankAccount;
import org.tekinico.easycount.domain.Category;
import org.tekinico.easycount.domain.Line;
import org.tekinico.easycount.domain.LineTemplate;
import org.tekinico.easycount.domain.enumeration.LineSource;
import org.tekinico.easycount.domain.enumeration.LineStatus;
import org.tekinico.easycount.domain.enumeration.TemplateFrequency;
import org.tekinico.easycount.repository.CategoryRepository;
import org.tekinico.easycount.repository.LineRepository;
import org.tekinico.easycount.repository.LineTemplateRepository;

import java.time.LocalDate;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Nico on 17/04/2017.
 */
@RunWith(MockitoJUnitRunner.class)
@Transactional
public class LineTemplateServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private LineTemplateRepository lineTemplateRepository;

    @Mock
    private LineRepository lineRepository;

    @InjectMocks
    private LineTemplateServiceImpl lineTemplateService;

    private LineTemplate tpl;

    @Before
    public void setUp() {
        Category cCreche = new Category();
        cCreche.setLabel("CrecheCat");

        BankAccount ba1 = new BankAccount();
        ba1.setId(7l);

        tpl = new LineTemplate();
        tpl.setId(119l);
        tpl.setActive(true);
        tpl.setBankAccount(ba1);
        tpl.addCategories(cCreche);
        tpl.setDayOfMonth(10);
        tpl.setFrequency(TemplateFrequency.MONTHLY);
        tpl.setDebit(84700d);
        tpl.setLabel("Creche");
        tpl.setStartDate(LocalDate.of(2017,1,1));

        when(lineTemplateRepository.findAllForGeneration(7l)).thenReturn(Arrays.asList(tpl));
    }

    @Test
    public void test_generateNewLines_monthly_date_future_create1Line() {

        // Given
        LocalDate asOf = LocalDate.of(2017,3,7);


        when(lineRepository.existsByTemplateAndDateGreaterThanEqualAndDateLessThanEqual(tpl,
            LocalDate.of(2017,3,1),
            LocalDate.of(2017,3,31)
        )).thenReturn(false);

        ArgumentCaptor<Line> lineCreated = ArgumentCaptor.forClass(Line.class);

        // When
        lineTemplateService.generateNewLinesForMonth(7l, asOf);

        // Then
        verify(lineRepository).save(lineCreated.capture());
        assertThat(lineCreated.getValue().getLabel()).isEqualTo("Creche");
        assertThat(lineCreated.getValue().getDate()).isEqualTo(LocalDate.of(2017,3,10));
        assertThat(lineCreated.getValue().getCreateDate()).isNotNull();
        assertThat(lineCreated.getValue().getDebit()).isEqualTo(84700d);
        assertThat(lineCreated.getValue().getCredit()).isNull();
        assertThat(lineCreated.getValue().getBankAccount().getId()).isEqualTo(7l);
        assertThat(lineCreated.getValue().getCategories()).extracting("label").contains("CrecheCat");
        assertThat(lineCreated.getValue().getSource()).isEqualTo(LineSource.GENERATED);
        assertThat(lineCreated.getValue().getStatus()).isEqualTo(LineStatus.NEW);
        assertThat(lineCreated.getValue().getTemplate().getId()).isEqualTo(119l);
    }

    @Test
    public void test_generateNewLines_monthly_date_past_dontCreate() {

        // Given
        LocalDate asOf = LocalDate.of(2017, 3, 15);

        // When
        lineTemplateService.generateNewLinesForMonth(7l, asOf);

        // Then
        verify(lineRepository, never()).save(any(Line.class));
        verify(lineRepository, never()).existsByTemplateAndDateGreaterThanEqualAndDateLessThanEqual(tpl,
            LocalDate.of(2017, 3, 1),
            LocalDate.of(2017, 3, 31));
    }

    @Test
    public void test_generateNewLines_monthly_date_future_alreadyExists_create1Line() {

        // Given
        LocalDate asOf = LocalDate.of(2017,3,8);

        when(lineRepository.existsByTemplateAndDateGreaterThanEqualAndDateLessThanEqual(tpl,
            LocalDate.of(2017,3,1),
            LocalDate.of(2017,3,31)
        )).thenReturn(true);

        // When
        lineTemplateService.generateNewLinesForMonth(7l, asOf);

        // Then
        verify(lineRepository, never()).save(any(Line.class));
    }
}
