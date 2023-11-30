package com.example.ookp.mapper;

import com.example.ookp.dto.CategoryDTO;
import com.example.ookp.model.Category;
import com.example.ookp.repository.CategoryRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@Component
@RequiredArgsConstructor
public class CategoryMapper {

    public Category toCategory(CategoryDTO categoryDTO) {
        var category = new Category();
        category.setId(categoryDTO.getId());
        category.setName(categoryDTO.getName());
        return category;
    }

    public CategoryDTO toCategoryDTO(Category category) {
        var categoryDTO = new CategoryDTO();
        categoryDTO.setId(category.getId());
        categoryDTO.setName(category.getName());
        return categoryDTO;
    }
}
