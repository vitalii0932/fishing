package com.example.ookp.mapper;

import com.example.ookp.model.Role;
import com.example.ookp.dto.RoleDTO;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@Component
@RequiredArgsConstructor
public class RoleMapper {
    public Role toRole(RoleDTO roleDTO) {
        var role = new Role();
        role.setId(roleDTO.getId());
        role.setName(roleDTO.getName());
        return role;
    }
    public RoleDTO toRoleDTO(Role role) {
        var roleDTO = new RoleDTO();
        roleDTO.setId(role.getId());
        roleDTO.setName(role.getName());
        return roleDTO;
    }
}
