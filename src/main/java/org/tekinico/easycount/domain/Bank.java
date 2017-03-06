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

/**
 * A Bank.
 */
@Entity
@Table(name = "bank")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Bank implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "label", nullable = false)
    private String label;

    @OneToMany(mappedBy = "bank")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<BankAccount> bankAccounts = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public Bank label(String label) {
        this.label = label;
        return this;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Set<BankAccount> getBankAccounts() {
        return bankAccounts;
    }

    public Bank bankAccounts(Set<BankAccount> bankAccounts) {
        this.bankAccounts = bankAccounts;
        return this;
    }

    public Bank addBankAccounts(BankAccount bankAccount) {
        this.bankAccounts.add(bankAccount);
        bankAccount.setBank(this);
        return this;
    }

    public Bank removeBankAccounts(BankAccount bankAccount) {
        this.bankAccounts.remove(bankAccount);
        bankAccount.setBank(null);
        return this;
    }

    public void setBankAccounts(Set<BankAccount> bankAccounts) {
        this.bankAccounts = bankAccounts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Bank bank = (Bank) o;
        if (bank.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, bank.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Bank{" +
            "id=" + id +
            ", label='" + label + "'" +
            '}';
    }
}
