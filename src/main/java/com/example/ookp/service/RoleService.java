package com.example.ookp.service;

import com.example.ookp.dto.RoleDTO;
import com.example.ookp.mapper.RoleMapper;
import com.example.ookp.model.Role;
import com.example.ookp.repository.RoleRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Data
@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    @Transactional(readOnly = true)
    public List<Role> getAll() {
        return roleRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Role findById(int id) {
        return roleRepository.findById(id).get();
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Retryable(maxAttempts = 5)
    public void add(RoleDTO roleDTO) {
        roleRepository.save(roleMapper.toRole(roleDTO));
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Retryable(maxAttempts = 5)
    public void deleteById(int id) {
        roleRepository.deleteById(id);
    }
}
