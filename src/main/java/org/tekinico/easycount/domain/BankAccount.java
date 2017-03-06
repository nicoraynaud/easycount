package org.tekinico.easycount.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import org.tekinico.easycount.domain.enumeration.BankAccountType;

/**
 * A BankAccount.
 */
@Entity
@Table(name = "bank_account")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class BankAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "label", nullable = false)
    private String label;

    @NotNull
    @Column(name = "number", nullable = false)
    private String number;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private BankAccountType type;

    @OneToMany(mappedBy = "bankAccount")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Line> lines = new HashSet<>();

    @ManyToOne
    private Bank bank;

    @ManyToOne
    private Currency currency;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public BankAccount label(String label) {
        this.label = label;
        return this;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getNumber() {
        return number;
    }

    public BankAccount number(String number) {
        this.number = number;
        return this;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public BankAccountType getType() {
        return type;
    }

    public BankAccount type(BankAccountType type) {
        this.type = type;
        return this;
    }

    public void setType(BankAccountType type) {
        this.type = type;
    }

    public Set<Line> getLines() {
        return lines;
    }

    public BankAccount lines(Set<Line> lines) {
        this.lines = lines;
        return this;
    }

    public BankAccount addLines(Line line) {
        this.lines.add(line);
        line.setBankAccount(this);
        return this;
    }

    public BankAccount removeLines(Line line) {
        this.lines.remove(line);
        line.setBankAccount(null);
        return this;
    }

    public void setLines(Set<Line> lines) {
        this.lines = lines;
    }

    public Bank getBank() {
        return bank;
    }

    public BankAccount bank(Bank bank) {
        this.bank = bank;
        return this;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    public Currency getCurrency() {
        return currency;
    }

    public BankAccount currency(Currency currency) {
        this.currency = currency;
        return this;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BankAccount bankAccount = (BankAccount) o;
        if (bankAccount.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, bankAccount.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "BankAccount{" +
            "id=" + id +
            ", label='" + label + "'" +
            ", number='" + number + "'" +
            ", type='" + type + "'" +
            '}';
    }
}
