package com.example.ookp.repository;

import com.example.ookp.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    @Query(value = "select * from products where name LIKE :text", nativeQuery = true)
    List<Product> findProductByName(@Param("text") String name);
    List<Product> findProductsByTypeId(int typeId);
    List<Product> findProductsByTypeName(String typeName);
    @Query(value = "select * from products where discount < price", nativeQuery = true)
    List<Product> findProductsWithDiscount();
    @Query(value = "select * from products order by total_sales desc limit :limit", nativeQuery = true)
    List<Product> findTopBySalesProducts(@Param("limit") int limit);
    @Query(value = "select products.id, products.name, image_path, type_id, price, discount, count, total_sales from products\n" +
            "join public.type t on t.id = products.type_id\n" +
            "where category_id = :categoryId", nativeQuery = true)
    List<Product> getProductsByCategoryId(@Param("categoryId") int categoryId);
}
