package com.example.ookp.service;

import com.example.ookp.dto.TypeDTO;
import com.example.ookp.mapper.TypeMapper;
import com.example.ookp.model.Type;
import com.example.ookp.repository.CategoryRepository;
import com.example.ookp.repository.TypeRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Data
@RequiredArgsConstructor
public class TypeService {
    private final TypeRepository typeRepository;
    private final CategoryRepository categoryRepository;
    private final TypeMapper typeMapper;

    @Transactional(readOnly = true)
    public List<Type> findAll() {
        return typeRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Type findById(int id) {
        return typeRepository.findById(id).get();
    }

    @Transactional(readOnly = true)
    public Type findByName(String name) {
        return typeRepository.findTypeByName(name);
    }

    @Transactional(readOnly = true)
    public List<Type> getTypesByCategoryId(int categoryId) {
        return typeRepository.getTypesNames(categoryId);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Retryable(maxAttempts = 5)
    public void add(TypeDTO typeDTO) {
        var category = typeMapper.toType(typeDTO);
        category.setCategory(categoryRepository.findById(typeDTO.getCategoryId()).get());
        typeRepository.save(category);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Retryable(maxAttempts = 5)
    public void deleteById(int id) {
        typeRepository.deleteById(id);
    }
}
