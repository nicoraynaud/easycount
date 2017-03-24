package org.tekinico.easycount.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.tekinico.easycount.EasycountApp;
import org.tekinico.easycount.domain.Category;
import org.tekinico.easycount.domain.Line;
import org.tekinico.easycount.domain.enumeration.LineSource;
import org.tekinico.easycount.domain.enumeration.LineStatus;
import org.tekinico.easycount.repository.CategoryRepository;
import org.tekinico.easycount.service.LineService;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Nico on 24/03/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = EasycountApp.class)
@Transactional
public class LineServiceImplTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private LineService lineService;


    @Before
    public void setUp() {
        Category cCreche = new Category();
        cCreche.setLabel("Creche");
        categoryRepository.save(cCreche);
    }

    @Test
    public void test_parseLines_returnsEmptyList() throws URISyntaxException, IOException {

        // Given
        File csv = new File(this.getClass().getResource("/files/import_line_test_empty.csv").toURI());
        MockMultipartFile file = new MockMultipartFile("original.csv", Files.readAllBytes(csv.toPath()));

        // When
        List<Line> result = lineService.parseLines(file);

        // Then
        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    public void test_parseLines_nullFile_returnsEmptyList() throws URISyntaxException, IOException {

        // Given
        MockMultipartFile file = null;

        // When
        List<Line> result = lineService.parseLines(file);

        // Then
        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    public void test_parseLines_returns3NewLines_OneCategryPresentTwice_OneCategoryPresentInDB() throws URISyntaxException, IOException {

        // Given
        File csv = new File(this.getClass().getResource("/files/import_line_test_4_lines.csv").toURI());
        MockMultipartFile file = new MockMultipartFile("original.csv", Files.readAllBytes(csv.toPath()));

        // When
        List<Line> result = lineService.parseLines(file);

        // Then
        assertThat(result.size()).isEqualTo(4);
        assertThat(result.get(0).getDate()).isEqualTo(LocalDate.of(2017, 4, 15));
        assertThat(result.get(0).getLabel()).isEqualTo("CHQ. J7 #3 1878484");
        assertThat(result.get(0).getDebit()).isEqualTo(202543.0);
        assertThat(result.get(0).getCredit()).isNull();
        assertThat(result.get(0).getSource()).isEqualTo(LineSource.MANUAL);
        assertThat(result.get(0).getStatus()).isEqualTo(LineStatus.NEW);
        assertThat(result.get(0).getCategories().iterator().next().getLabel()).isEqualTo("Travaux");

        assertThat(result.get(1).getDate()).isEqualTo(LocalDate.of(2017, 3, 28));
        assertThat(result.get(1).getLabel()).isEqualTo("Prlv. OPT Mobilis");
        assertThat(result.get(1).getDebit()).isEqualTo(2500.0);
        assertThat(result.get(1).getCredit()).isNull();
        assertThat(result.get(1).getSource()).isEqualTo(LineSource.MANUAL);
        assertThat(result.get(1).getStatus()).isEqualTo(LineStatus.NEW);
        assertThat(result.get(1).getCategories().iterator().next().getLabel()).isEqualTo("Mobilis");

        assertThat(result.get(2).getDate()).isEqualTo(LocalDate.of(2017, 3, 16));
        assertThat(result.get(2).getLabel()).isEqualTo("Aide Crêche MDF");
        assertThat(result.get(2).getDebit()).isNull();
        assertThat(result.get(2).getCredit()).isEqualTo(9900.0);
        assertThat(result.get(2).getSource()).isEqualTo(LineSource.MANUAL);
        assertThat(result.get(2).getStatus()).isEqualTo(LineStatus.NEW);
        assertThat(result.get(2).getCategories().iterator().next().getLabel()).isEqualTo("Creche");

        assertThat(result.get(3).getDate()).isEqualTo(LocalDate.of(2017, 3, 15));
        assertThat(result.get(3).getLabel()).isEqualTo("CHQ. J7 #2 1878481");
        assertThat(result.get(3).getDebit()).isEqualTo(200000.0);
        assertThat(result.get(3).getCredit()).isNull();
        assertThat(result.get(3).getSource()).isEqualTo(LineSource.MANUAL);
        assertThat(result.get(3).getStatus()).isEqualTo(LineStatus.TICKED);
        assertThat(result.get(3).getCategories().iterator().next().getLabel()).isEqualTo("Travaux");

        List<Category> categories = categoryRepository.findAll();
        assertThat(categories.size()).isEqualTo(3);
    }

}
