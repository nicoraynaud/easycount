package org.tekinico.easycount.repository;

import net.sf.cglib.core.Local;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.tekinico.easycount.EasycountApp;
import org.tekinico.easycount.domain.*;
import org.tekinico.easycount.domain.enumeration.BankAccountType;
import org.tekinico.easycount.domain.enumeration.LineSource;
import org.tekinico.easycount.domain.enumeration.LineStatus;
import org.tekinico.easycount.domain.enumeration.TemplateFrequency;
import org.tekinico.easycount.repository.*;
import org.tekinico.easycount.service.LineService;
import org.tekinico.easycount.service.dto.LineDTO;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Nico on 24/03/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = EasycountApp.class)
@Transactional
public class LineRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BankRepository bankRepository;

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private LineTemplateRepository lineTemplateRepository;

    @Autowired
    private LineRepository lineRepository;

    @Test
    public void test_banlanceOfAccount_calculateBalanceOfAccount() {
        // Given
        Bank b = new Bank();
        b.setLabel("BCI");
        bankRepository.saveAndFlush(b);

        Currency c = new Currency();
        c.setCode("XPF");
        c.setLabel("FP");
        c.setDecimals(0);
        c.setSymbol("XPF");
        currencyRepository.saveAndFlush(c);

        BankAccount ba = new BankAccount();
        ba.setLabel("BCI");
        ba.setBank(b);
        ba.setCurrency(c);
        ba.setNumber("1");
        ba.setType(BankAccountType.CHECKING);
        bankAccountRepository.saveAndFlush(ba);

        // - 9 000
        Line line1 = new Line();
        line1.setCreateDate(ZonedDateTime.now());
        line1.setLabel("debit 1");
        line1.setDebit(9000.0);
        line1.setDate(LocalDate.of(2017,3,9));
        line1.setStatus(LineStatus.NEW);
        line1.setSource(LineSource.MANUAL);
        line1.setBankAccount(ba);
        lineRepository.saveAndFlush(line1);

        // - 10 000
        Line line2 = new Line();
        line2.setCreateDate(ZonedDateTime.now());
        line2.setLabel("debit 2");
        line2.setDebit(10000.0);
        line2.setDate(LocalDate.of(2017,3,9));
        line2.setStatus(LineStatus.NEW);
        line2.setSource(LineSource.MANUAL);
        line2.setBankAccount(ba);
        lineRepository.saveAndFlush(line2);

        // + 5 000
        Line line3 = new Line();
        line3.setCreateDate(ZonedDateTime.now());
        line3.setLabel("credit 3");
        line3.setCredit(15000.0);
        line3.setDate(LocalDate.of(2017,3,9));
        line3.setStatus(LineStatus.NEW);
        line3.setSource(LineSource.MANUAL);
        line3.setBankAccount(ba);
        lineRepository.saveAndFlush(line3);

        // + 6 000 --not taken
        Line line4 = new Line();
        line4.setCreateDate(ZonedDateTime.now());
        line4.setLabel("credit 4");
        line4.setCredit(6000.0);
        line4.setDate(LocalDate.of(2018,3,9));
        line4.setStatus(LineStatus.NEW);
        line4.setSource(LineSource.MANUAL);
        line4.setBankAccount(ba);
        lineRepository.saveAndFlush(line4);

        // - 10 000
        Line line5 = new Line();
        line5.setCreateDate(ZonedDateTime.now());
        line5.setLabel("debit 5");
        line5.setDebit(10000.0);
        line5.setDate(LocalDate.of(2017,3,7));
        line5.setStatus(LineStatus.NEW);
        line5.setSource(LineSource.MANUAL);
        line5.setBankAccount(ba);
        lineRepository.saveAndFlush(line5);

        // - 150 000 --not taken
        Line line6 = new Line();
        line6.setCreateDate(ZonedDateTime.now());
        line6.setLabel("debit 6");
        line6.setDebit(150000.0);
        line6.setDate(LocalDate.of(2017,3,7));
        line6.setStatus(LineStatus.CANCELLED);
        line6.setSource(LineSource.MANUAL);
        line6.setBankAccount(ba);
        lineRepository.saveAndFlush(line6);

        // When
        Double balance = lineRepository.banlanceOfAccount(ba.getId(), LocalDate.of(2017,4,15));

        // Then
        assertThat(balance).isEqualTo(-14000.0);
    }

    @Test
    public void test_existsByTemplateAndDateGreaterThanEqualAndDateLessThanEqual_false() {
        // Given
        Bank b = new Bank();
        b.setLabel("BCI");
        bankRepository.saveAndFlush(b);

        Currency c = new Currency();
        c.setCode("XPF");
        c.setLabel("FP");
        c.setDecimals(0);
        c.setSymbol("XPF");
        currencyRepository.saveAndFlush(c);

        BankAccount ba = new BankAccount();
        ba.setLabel("BCI");
        ba.setBank(b);
        ba.setCurrency(c);
        ba.setNumber("1");
        ba.setType(BankAccountType.CHECKING);
        bankAccountRepository.saveAndFlush(ba);

        LineTemplate tpl = new LineTemplate();
        tpl.setLabel("l");
        tpl.setStartDate(LocalDate.of(2017,1,1));
        tpl.setFrequency(TemplateFrequency.MONTHLY);
        lineTemplateRepository.saveAndFlush(tpl);

        // 2017-03-09 - generated
        Line line1 = new Line();
        line1.setCreateDate(ZonedDateTime.now());
        line1.setLabel("debit 1");
        line1.setDebit(9000.0);
        line1.setDate(LocalDate.of(2017,3,9));
        line1.setStatus(LineStatus.NEW);
        line1.setSource(LineSource.GENERATED);
        line1.setBankAccount(ba);
        line1.setTemplate(tpl);
        lineRepository.saveAndFlush(line1);


        LineTemplate tpl2 = new LineTemplate();
        tpl2.setLabel("2");
        tpl2.setStartDate(LocalDate.of(2017,1,1));
        tpl2.setFrequency(TemplateFrequency.MONTHLY);
        lineTemplateRepository.saveAndFlush(tpl2);

        // 2017-03-16 - generated
        Line line2 = new Line();
        line2.setCreateDate(ZonedDateTime.now());
        line2.setLabel("debit 2");
        line2.setDebit(10000.0);
        line2.setDate(LocalDate.of(2017,3,16));
        line2.setStatus(LineStatus.NEW);
        line2.setSource(LineSource.GENERATED);
        line2.setBankAccount(ba);
        line2.setTemplate(tpl2);
        lineRepository.saveAndFlush(line2);

        // 2017-03-16 - manual
        Line line3 = new Line();
        line3.setCreateDate(ZonedDateTime.now());
        line3.setLabel("debit 3");
        line3.setDebit(10000.0);
        line3.setDate(LocalDate.of(2017,3,16));
        line3.setStatus(LineStatus.NEW);
        line3.setSource(LineSource.MANUAL);
        line3.setBankAccount(ba);
        lineRepository.saveAndFlush(line3);

        // When
        boolean result = lineRepository.existsByTemplateAndDateGreaterThanEqualAndDateLessThanEqual(
            tpl, LocalDate.of(2017,3,15), LocalDate.of(2017,3,31));

        // Then
        assertThat(result).isFalse();
    }

    @Test
    public void test_existsByTemplateAndDateGreaterThanEqualAndDateLessThanEqual_true() {
        // Given
        Bank b = new Bank();
        b.setLabel("BCI");
        bankRepository.saveAndFlush(b);

        Currency c = new Currency();
        c.setCode("XPF");
        c.setLabel("FP");
        c.setDecimals(0);
        c.setSymbol("XPF");
        currencyRepository.saveAndFlush(c);

        BankAccount ba = new BankAccount();
        ba.setLabel("BCI");
        ba.setBank(b);
        ba.setCurrency(c);
        ba.setNumber("1");
        ba.setType(BankAccountType.CHECKING);
        bankAccountRepository.saveAndFlush(ba);

        LineTemplate tpl = new LineTemplate();
        tpl.setLabel("2");
        tpl.setStartDate(LocalDate.of(2017,1,1));
        tpl.setFrequency(TemplateFrequency.MONTHLY);
        lineTemplateRepository.saveAndFlush(tpl);

        // 2017-03-09 - generated
        Line line1 = new Line();
        line1.setCreateDate(ZonedDateTime.now());
        line1.setLabel("debit 1");
        line1.setDebit(9000.0);
        line1.setDate(LocalDate.of(2017,3,9));
        line1.setStatus(LineStatus.NEW);
        line1.setSource(LineSource.GENERATED);
        line1.setBankAccount(ba);
        line1.setTemplate(tpl);
        lineRepository.saveAndFlush(line1);


        LineTemplate tpl2 = new LineTemplate();
        tpl2.setLabel("2");
        tpl2.setStartDate(LocalDate.of(2017,1,1));
        tpl2.setFrequency(TemplateFrequency.MONTHLY);
        lineTemplateRepository.saveAndFlush(tpl2);

        // 2017-03-16 - generated
        Line line2 = new Line();
        line2.setCreateDate(ZonedDateTime.now());
        line2.setLabel("debit 2");
        line2.setDebit(10000.0);
        line2.setDate(LocalDate.of(2017,3,16));
        line2.setStatus(LineStatus.NEW);
        line2.setSource(LineSource.GENERATED);
        line2.setBankAccount(ba);
        line2.setTemplate(tpl2);
        lineRepository.saveAndFlush(line2);

        // 2017-03-16 - manual
        Line line3 = new Line();
        line3.setCreateDate(ZonedDateTime.now());
        line3.setLabel("debit 3");
        line3.setDebit(10000.0);
        line3.setDate(LocalDate.of(2017,3,16));
        line3.setStatus(LineStatus.NEW);
        line3.setSource(LineSource.MANUAL);
        line3.setBankAccount(ba);
        lineRepository.saveAndFlush(line3);

        // When
        boolean result = lineRepository.existsByTemplateAndDateGreaterThanEqualAndDateLessThanEqual(
            tpl2, LocalDate.of(2017,3,1), LocalDate.of(2017,3,16));

        // Then
        assertThat(result).isTrue();
    }
}
