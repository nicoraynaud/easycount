package org.tekinico.easycount.service.mapper;

import org.tekinico.easycount.domain.*;
import org.tekinico.easycount.service.dto.CategoryDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Category and its DTO CategoryDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CategoryMapper {

    @Mapping(source = "parentCategory.id", target = "parentCategoryId")
    CategoryDTO categoryToCategoryDTO(Category category);

    List<CategoryDTO> categoriesToCategoryDTOs(List<Category> categories);

    @Mapping(source = "parentCategoryId", target = "parentCategory")
    Category categoryDTOToCategory(CategoryDTO categoryDTO);

    List<Category> categoryDTOsToCategories(List<CategoryDTO> categoryDTOs);

    default Category CategoryFromId(Long id) {
        if (id == null) {
            return null;
        }
        Category Category = new Category();
        Category.setId(id);
        return Category;
    }
}
