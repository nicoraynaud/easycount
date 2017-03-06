package org.tekinico.easycount.service.dto;

import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import org.tekinico.easycount.domain.enumeration.LineStatus;
import org.tekinico.easycount.domain.enumeration.LineSource;

/**
 * A DTO for the Line entity.
 */
public class LineDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate date;

    @NotNull
    private String label;

    private Double debit;

    private Double credit;

    private Double balance;

    @NotNull
    private LineStatus status;

    private LineSource source;

    private Set<CategoryDTO> categories = new HashSet<>();

    private Long bankAccountId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
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
    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }
    public LineStatus getStatus() {
        return status;
    }

    public void setStatus(LineStatus status) {
        this.status = status;
    }
    public LineSource getSource() {
        return source;
    }

    public void setSource(LineSource source) {
        this.source = source;
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

        LineDTO lineDTO = (LineDTO) o;

        if ( ! Objects.equals(id, lineDTO.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "LineDTO{" +
            "id=" + id +
            ", date='" + date + "'" +
            ", label='" + label + "'" +
            ", debit='" + debit + "'" +
            ", credit='" + credit + "'" +
            ", balance='" + balance + "'" +
            ", status='" + status + "'" +
            ", source='" + source + "'" +
            '}';
    }
}
