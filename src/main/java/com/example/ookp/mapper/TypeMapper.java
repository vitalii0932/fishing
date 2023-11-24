package com.example.ookp.mapper;

import com.example.ookp.dto.TypeDTO;
import com.example.ookp.model.Type;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TypeMapper {
    Type toType(TypeDTO typeDTO);
    TypeDTO toTypeDTO(Type type);
}
