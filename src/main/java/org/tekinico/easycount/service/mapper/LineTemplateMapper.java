package org.tekinico.easycount.service.mapper;

import org.tekinico.easycount.domain.*;
import org.tekinico.easycount.service.dto.LineTemplateDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity LineTemplate and its DTO LineTemplateDTO.
 */
@Mapper(componentModel = "spring", uses = {CategoryMapper.class, BankAccountMapper.class, })
public interface LineTemplateMapper {

    @Mapping(source = "bankAccount.id", target = "bankAccountId")
    LineTemplateDTO lineTemplateToLineTemplateDTO(LineTemplate lineTemplate);

    List<LineTemplateDTO> lineTemplatesToLineTemplateDTOs(List<LineTemplate> lineTemplates);

    @Mapping(source = "bankAccountId", target = "bankAccount")
    LineTemplate lineTemplateDTOToLineTemplate(LineTemplateDTO lineTemplateDTO);

    List<LineTemplate> lineTemplateDTOsToLineTemplates(List<LineTemplateDTO> lineTemplateDTOs);
    /**
     * generating the fromId for all mappers if the databaseType is sql, as the class has relationship to it might need it, instead of
     * creating a new attribute to know if the entity has any relationship from some other entity
     *
     * @param id id of the entity
     * @return the entity instance
     */

    default LineTemplate lineTemplateFromId(Long id) {
        if (id == null) {
            return null;
        }
        LineTemplate lineTemplate = new LineTemplate();
        lineTemplate.setId(id);
        return lineTemplate;
    }

    default BankAccount bankAccountFromId(Long id) {
        if (id == null) {
            return null;
        }
        BankAccount bankAccount = new BankAccount();
        bankAccount.setId(id);
        return bankAccount;
    }

}
