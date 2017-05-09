package org.tekinico.easycount.service.mapper;

import org.tekinico.easycount.domain.*;
import org.tekinico.easycount.service.dto.LineDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Line and its DTO LineDTO.
 */
@Mapper(componentModel = "spring", uses = {CategoryMapper.class, BankAccountMapper.class, LineTemplateMapper.class, })
public interface LineMapper {

    @Mapping(source = "bankAccount.id", target = "bankAccountId")
    @Mapping(source = "template.id", target = "templateId")
    LineDTO lineToLineDTO(Line line);

    List<LineDTO> linesToLineDTOs(List<Line> lines);

    @Mapping(source = "bankAccountId", target = "bankAccount")
    @Mapping(source = "templateId", target = "template")
    Line lineDTOToLine(LineDTO lineDTO);

    List<Line> lineDTOsToLines(List<LineDTO> lineDTOs);
    /**
     * generating the fromId for all mappers if the databaseType is sql, as the class has relationship to it might need it, instead of
     * creating a new attribute to know if the entity has any relationship from some other entity
     *
     * @param id id of the entity
     * @return the entity instance
     */
     
    default Line lineFromId(Long id) {
        if (id == null) {
            return null;
        }
        Line line = new Line();
        line.setId(id);
        return line;
    }
    

}
