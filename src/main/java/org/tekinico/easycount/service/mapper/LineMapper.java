package org.tekinico.easycount.service.mapper;

import org.tekinico.easycount.domain.*;
import org.tekinico.easycount.service.dto.LineDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Line and its DTO LineDTO.
 */
@Mapper(componentModel = "spring", uses = {CategoryMapper.class, })
public interface LineMapper {

    @Mapping(source = "bankAccount.id", target = "bankAccountId")
    @Mapping(target = "bankAccountLabel", expression = "java(line.getBankAccount() != null ? line.getBankAccount().getBank().getLabel() + \" - \" + line.getBankAccount().getLabel() : \"\")")
    LineDTO lineToLineDTO(Line line);

    List<LineDTO> linesToLineDTOs(List<Line> lines);

    @Mapping(source = "bankAccountId", target = "bankAccount")
    Line lineDTOToLine(LineDTO lineDTO);

    List<Line> lineDTOsToLines(List<LineDTO> lineDTOs);

    default Category categoryFromId(Long id) {
        if (id == null) {
            return null;
        }
        Category category = new Category();
        category.setId(id);
        return category;
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
