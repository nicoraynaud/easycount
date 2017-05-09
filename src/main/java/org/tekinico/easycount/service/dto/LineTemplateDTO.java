package org.tekinico.easycount.service.dto;

import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import org.tekinico.easycount.domain.enumeration.TemplateFrequency;

/**
 * A DTO for the LineTemplate entity.
 */
public class LineTemplateDTO implements Serializable {

    private Long id;

    @NotNull
    private String label;

    private Double debit;

    private Double credit;

    private Boolean active;

    private Integer dayOfMonth;

    @NotNull
    private TemplateFrequency frequency;

    @NotNull
    private LocalDate startDate;

    private Integer occurrences;

    private Set<CategoryDTO> categories = new HashSet<>();

    private Long bankAccountId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
    public Double getDebit() {
        return debit;
    }

    public void setDebit(Double debit) {
        this.debit = debit;
    }
    public Double getCredit() {
        return credit;
    }

    public void setCredit(Double credit) {
        this.credit = credit;
    }
    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
    public Integer getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(Integer dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }
    public TemplateFrequency getFrequency() {
        return frequency;
    }

    public void setFrequency(TemplateFrequency frequency) {
        this.frequency = frequency;
    }
    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
    public Integer getOccurrences() {
        return occurrences;
    }

    public void setOccurrences(Integer occurrences) {
        this.occurrences = occurrences;
    }

    public Set<CategoryDTO> getCategories() {
        return categories;
    }

    public void setCategories(Set<CategoryDTO> categories) {
        this.categories = categories;
    }

    public Long getBankAccountId() {
        return bankAccountId;
    }

    public void setBankAccountId(Long bankAccountId) {
        this.bankAccountId = bankAccountId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        LineTemplateDTO lineTemplateDTO = (LineTemplateDTO) o;

        if ( ! Objects.equals(id, lineTemplateDTO.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "LineTemplateDTO{" +
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
