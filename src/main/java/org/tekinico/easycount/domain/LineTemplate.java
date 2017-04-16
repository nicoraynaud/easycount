package org.tekinico.easycount.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import org.tekinico.easycount.domain.enumeration.TemplateFrequency;

/**
 * A LineTemplate.
 */
@Entity
@Table(name = "line_template")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "linetemplate")
public class LineTemplate implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "jhi_label", nullable = false)
    private String label;

    @Column(name = "debit")
    private Double debit;

    @Column(name = "credit")
    private Double credit;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "day_of_month")
    private Integer dayOfMonth;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "frequency", nullable = false)
    private TemplateFrequency frequency;

    @NotNull
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "occurrences")
    private Integer occurrences;

    @OneToMany(mappedBy = "template")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Line> lines = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "line_template_categories",
               joinColumns = @JoinColumn(name="line_templates_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="categories_id", referencedColumnName="id"))
    private Set<Category> categories = new HashSet<>();

    @ManyToOne
    private BankAccount bankAccount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public LineTemplate label(String label) {
        this.label = label;
        return this;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Double getDebit() {
        return debit;
    }

    public LineTemplate debit(Double debit) {
        this.debit = debit;
        return this;
    }

    public void setDebit(Double debit) {
        this.debit = debit;
    }

    public Double getCredit() {
        return credit;
    }

    public LineTemplate credit(Double credit) {
        this.credit = credit;
        return this;
    }

    public void setCredit(Double credit) {
        this.credit = credit;
    }

    public Boolean isActive() {
        return active;
    }

    public LineTemplate active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Integer getDayOfMonth() {
        return dayOfMonth;
    }

    public LineTemplate dayOfMonth(Integer dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
        return this;
    }

    public void setDayOfMonth(Integer dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public TemplateFrequency getFrequency() {
        return frequency;
    }

    public LineTemplate frequency(TemplateFrequency frequency) {
        this.frequency = frequency;
        return this;
    }

    public void setFrequency(TemplateFrequency frequency) {
        this.frequency = frequency;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LineTemplate startDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public Integer getOccurrences() {
        return occurrences;
    }

    public LineTemplate occurrences(Integer occurrences) {
        this.occurrences = occurrences;
        return this;
    }

    public void setOccurrences(Integer occurrences) {
        this.occurrences = occurrences;
    }

    public Set<Line> getLines() {
        return lines;
    }

    public LineTemplate lines(Set<Line> lines) {
        this.lines = lines;
        return this;
    }

    public LineTemplate addLines(Line line) {
        this.lines.add(line);
        line.setTemplate(this);
        return this;
    }

    public LineTemplate removeLines(Line line) {
        this.lines.remove(line);
        line.setTemplate(null);
        return this;
    }

    public void setLines(Set<Line> lines) {
        this.lines = lines;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public LineTemplate categories(Set<Category> categories) {
        this.categories = categories;
        return this;
    }

    public LineTemplate addCategories(Category category) {
        this.categories.add(category);
        return this;
    }

    public LineTemplate removeCategories(Category category) {
        this.categories.remove(category);
        return this;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    public BankAccount getBankAccount() {
        return bankAccount;
    }

    public LineTemplate bankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
        return this;
    }

    public void setBankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LineTemplate lineTemplate = (LineTemplate) o;
        if (lineTemplate.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, lineTemplate.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "LineTemplate{" +
            "id=" + id +
            ", label='" + label + "'" +
            ", debit='" + debit + "'" +
            ", credit='" + credit + "'" +
            ", active='" + active + "'" +
            ", dayOfMonth='" + dayOfMonth + "'" +
            ", frequency='" + frequency + "'" +
            ", startDate='" + startDate + "'" +
            ", occurrences='" + occurrences + "'" +
            '}';
    }
}
