package org.tekinico.easycount.service.mapper;

import org.tekinico.easycount.domain.*;
import org.tekinico.easycount.service.dto.BankAccountDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity BankAccount and its DTO BankAccountDTO.
 */
@Mapper(componentModel = "spring", uses = {BankMapper.class, CurrencyMapper.class, })
public interface BankAccountMapper {

    @Mapping(source = "bank.id", target = "bankId")
    @Mapping(source = "currency.id", target = "currencyId")
    BankAccountDTO bankAccountToBankAccountDTO(BankAccount bankAccount);

    List<BankAccountDTO> bankAccountsToBankAccountDTOs(List<BankAccount> bankAccounts);

    @Mapping(target = "lines", ignore = true)
    @Mapping(source = "bankId", target = "bank")
    @Mapping(source = "currencyId", target = "currency")
    BankAccount bankAccountDTOToBankAccount(BankAccountDTO bankAccountDTO);

    List<BankAccount> bankAccountDTOsToBankAccounts(List<BankAccountDTO> bankAccountDTOs);
    /**
     * generating the fromId for all mappers if the databaseType is sql, as the class has relationship to it might need it, instead of
     * creating a new attribute to know if the entity has any relationship from some other entity
     *
     * @param id id of the entity
     * @return the entity instance
     */
     
    default BankAccount bankAccountFromId(Long id) {
        if (id == null) {
            return null;
        }
        BankAccount bankAccount = new BankAccount();
        bankAccount.setId(id);
        return bankAccount;
    }
    

}
