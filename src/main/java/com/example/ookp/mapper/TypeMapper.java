package com.example.ookp.mapper;

import com.example.ookp.dto.TypeDTO;
import com.example.ookp.model.Type;
import com.example.ookp.repository.CategoryRepository;
import com.example.ookp.service.CategoryService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@Component
@RequiredArgsConstructor
public class TypeMapper {
    private final CategoryService categoryService;

    public Type toType(TypeDTO typeDTO) {
        var type = new Type();
        type.setId(typeDTO.getId());
        type.setName(typeDTO.getName());
        if(typeDTO.getCategoryId() == 0) {
            type.setCategory(null);
        } else {
            type.setCategory(categoryService.findById(typeDTO.getCategoryId()));
        }
        return type;
    }
    public TypeDTO toTypeDTO(Type type) {
        var typeDTO = new TypeDTO();
        typeDTO.setId(type.getId());
        typeDTO.setName(type.getName());
        if(type.getCategory() == null) {
            typeDTO.setCategoryId(0);
        } else {
            typeDTO.setCategoryId(type.getCategory().getId());
        }
        return typeDTO;
    }
}
