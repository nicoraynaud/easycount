package org.tekinico.easycount.service.mapper;

import org.tekinico.easycount.domain.*;
import org.tekinico.easycount.service.dto.CurrencyDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Currency and its DTO CurrencyDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CurrencyMapper {

    CurrencyDTO currencyToCurrencyDTO(Currency currency);

    List<CurrencyDTO> currenciesToCurrencyDTOs(List<Currency> currencies);

    Currency currencyDTOToCurrency(CurrencyDTO currencyDTO);

    List<Currency> currencyDTOsToCurrencies(List<CurrencyDTO> currencyDTOs);
}
