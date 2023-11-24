package com.example.ookp.mapper;

import com.example.ookp.dto.CategoryDTO;
import com.example.ookp.model.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category toCategory(CategoryDTO categoryDTO);
    CategoryDTO toCategoryDTO(Category category);
}
