package com.example.ookp.repository;

import com.example.ookp.model.Type;
import com.example.ookp.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TypeRepository extends JpaRepository<Type, Integer> {
    Type findTypeByName(String name);
    @Query(value = "select * from type where category_id = :categoryId", nativeQuery = true)
    List<Type> getTypesIds(@Param("categoryId") int categoryId);

    @Query(value = "select * from type where category_id = :categoryId", nativeQuery = true)
    List<Type> getTypesNames(@Param("categoryId") int categoryId);
}
