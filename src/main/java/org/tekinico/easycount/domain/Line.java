package org.tekinico.easycount.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import org.tekinico.easycount.domain.enumeration.LineStatus;

import org.tekinico.easycount.domain.enumeration.LineSource;

/**
 * A Line.
 */
@Entity
@Table(name = "line")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Line implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @NotNull
    @Column(name = "label", nullable = false)
    private String label;

    @Column(name = "debit")
    private Double debit;

    @Column(name = "credit")
    private Double credit;

    @Column(name = "balance")
    private Double balance;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private LineStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "source")
    private LineSource source;

    @NotNull
    @Column(name = "create_date", nullable = false)
    private ZonedDateTime createDate;

    @Column(name = "effective_date")
    private LocalDate effectiveDate;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "line_categories",
               joinColumns = @JoinColumn(name="lines_id", referencedColumnName="id"),
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

    public LocalDate getDate() {
        return date;
    }

    public Line date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getLabel() {
        return label;
    }

    public Line label(String label) {
        this.label = label;
        return this;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Double getDebit() {
        return debit;
    }

    public Line debit(Double debit) {
        this.debit = debit;
        return this;
    }

    public void setDebit(Double debit) {
        this.debit = debit;
    }

    public Double getCredit() {
        return credit;
    }

    public Line credit(Double credit) {
        this.credit = credit;
        return this;
    }

    public void setCredit(Double credit) {
        this.credit = credit;
    }

    public Double getBalance() {
        return balance;
    }

    public Line balance(Double balance) {
        this.balance = balance;
        return this;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public LineStatus getStatus() {
        return status;
    }

    public Line status(LineStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(LineStatus status) {
        this.status = status;
    }

    public LineSource getSource() {
        return source;
    }

    public Line source(LineSource source) {
        this.source = source;
        return this;
    }

    public void setSource(LineSource source) {
        this.source = source;
    }

    public ZonedDateTime getCreateDate() {
        return createDate;
    }

    public Line createDate(ZonedDateTime createDate) {
        this.createDate = createDate;
        return this;
    }

    public void setCreateDate(ZonedDateTime createDate) {
        this.createDate = createDate;
    }

    public LocalDate getEffectiveDate() {
        return effectiveDate;
    }

    public Line effectiveDate(LocalDate effectiveDate) {
        this.effectiveDate = effectiveDate;
        return this;
    }

    public void setEffectiveDate(LocalDate effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public Line categories(Set<Category> categories) {
        this.categories = categories;
        return this;
    }

    public Line addCategories(Category category) {
        this.categories.add(category);
        return this;
    }

    public Line removeCategories(Category category) {
        this.categories.remove(category);
        return this;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    public BankAccount getBankAccount() {
        return bankAccount;
    }

    public Line bankAccount(BankAccount bankAccount) {
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
        Line line = (Line) o;
        if (line.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, line.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Line{" +
            "id=" + id +
            ", date='" + date + "'" +
            ", label='" + label + "'" +
            ", debit='" + debit + "'" +
            ", credit='" + credit + "'" +
            ", balance='" + balance + "'" +
            ", status='" + status + "'" +
            ", source='" + source + "'" +
            ", createDate='" + createDate + "'" +
            ", effectiveDate='" + effectiveDate + "'" +
            '}';
    }
}
