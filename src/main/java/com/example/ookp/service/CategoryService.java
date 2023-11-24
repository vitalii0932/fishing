package com.example.ookp.service;

import com.example.ookp.dto.CategoryDTO;
import com.example.ookp.mapper.CategoryMapper;
import com.example.ookp.model.Category;
import com.example.ookp.repository.CategoryRepository;
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
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Transactional(readOnly = true)
    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Category findById(int id) {
        return categoryRepository.findById(id).get();
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Retryable(maxAttempts = 5)
    public void add(CategoryDTO categoryDTO) {
        categoryRepository.save(categoryMapper.toCategory(categoryDTO));
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Retryable(maxAttempts = 5)
    public void deleteById(int id) {
        categoryRepository.deleteById(id);
    }
}
