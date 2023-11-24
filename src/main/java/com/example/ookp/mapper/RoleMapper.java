package com.example.ookp.mapper;

import com.example.ookp.dto.RoleDTO;
import com.example.ookp.model.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    Role toRole(RoleDTO roleDTO);
    RoleDTO toRoleDTO(Role role);
}
