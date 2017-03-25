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
    /**
     * generating the fromId for all mappers if the databaseType is sql, as the class has relationship to it might need it, instead of
     * creating a new attribute to know if the entity has any relationship from some other entity
     *
     * @param id id of the entity
     * @return the entity instance
     */
     
    default Currency currencyFromId(Long id) {
        if (id == null) {
            return null;
        }
        Currency currency = new Currency();
        currency.setId(id);
        return currency;
    }
    

}
