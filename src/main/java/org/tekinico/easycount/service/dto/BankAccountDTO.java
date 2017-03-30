package org.tekinico.easycount.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import org.tekinico.easycount.domain.enumeration.BankAccountType;

/**
 * A DTO for the BankAccount entity.
 */
public class BankAccountDTO implements Serializable {

    private Long id;

    @NotNull
    private String label;

    @NotNull
    private String number;

    @NotNull
    private BankAccountType type;

    private Long bankId;

    private Long currencyId;

    private String currencyLabel;

    private String bankLabel;

    private Double balance;

    private Double balanceTicked;

    private Double balanceEom;

    private Double balanceEomP1;

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
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
    public BankAccountType getType() {
        return type;
    }

    public void setType(BankAccountType type) {
        this.type = type;
    }

    public Long getBankId() {
        return bankId;
    }

    public void setBankId(Long bankId) {
        this.bankId = bankId;
    }

    public Long getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Long currencyId) {
        this.currencyId = currencyId;
    }

    public String getCurrencyLabel() {
        return currencyLabel;
    }

    public void setCurrencyLabel(String currencyLabel) {
        this.currencyLabel = currencyLabel;
    }

    public String getBankLabel() {
        return bankLabel;
    }

    public void setBankLabel(String bankLabel) {
        this.bankLabel = bankLabel;
    }

    public Double getBalance() {
        return balance;
    }

    public Double getBalanceTicked() {
        return balanceTicked;
    }

    public void setBalanceTicked(Double balanceTicked) {
        this.balanceTicked = balanceTicked;
    }

    public Double getBalanceEom() {
        return balanceEom;
    }

    public void setBalanceEom(Double balanceEom) {
        this.balanceEom = balanceEom;
    }

    public Double getBalanceEomP1() {
        return balanceEomP1;
    }

    public void setBalanceEomP1(Double balanceEomP1) {
        this.balanceEomP1 = balanceEomP1;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BankAccountDTO bankAccountDTO = (BankAccountDTO) o;

        if ( ! Objects.equals(id, bankAccountDTO.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "BankAccountDTO{" +
            "id=" + id +
            ", label='" + label + "'" +
            ", number='" + number + "'" +
            ", type='" + type + "'" +
            '}';
    }
}
