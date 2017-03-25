package org.tekinico.easycount.service.mapper;

import org.tekinico.easycount.domain.*;
import org.tekinico.easycount.service.dto.BankDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Bank and its DTO BankDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface BankMapper {

    BankDTO bankToBankDTO(Bank bank);

    List<BankDTO> banksToBankDTOs(List<Bank> banks);

    @Mapping(target = "bankAccounts", ignore = true)
    Bank bankDTOToBank(BankDTO bankDTO);

    List<Bank> bankDTOsToBanks(List<BankDTO> bankDTOs);
    /**
     * generating the fromId for all mappers if the databaseType is sql, as the class has relationship to it might need it, instead of
     * creating a new attribute to know if the entity has any relationship from some other entity
     *
     * @param id id of the entity
     * @return the entity instance
     */
     
    default Bank bankFromId(Long id) {
        if (id == null) {
            return null;
        }
        Bank bank = new Bank();
        bank.setId(id);
        return bank;
    }
    

}
