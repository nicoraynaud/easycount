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
}
