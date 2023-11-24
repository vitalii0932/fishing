package com.example.ookp.repository;

import com.example.ookp.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    @Query(value = "select c.id, c.name, count(p) from category c\n" +
            "join public.type t on c.id = t.category_id\n" +
            "join public.products p on t.id = p.type_id\n" +
            "group by c.id\n" +
            "order by c.id;", nativeQuery = true)
    List<String> getProductsCount();
}
