package com.example.ookp.repository;

import com.example.ookp.model.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.example.ookp.model.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    @Query(value = "select shopping_cart_id from users where id = :customerId", nativeQuery = true)
    ShoppingCart getShoppingCart(@Param("customerId") int customerId);
    @Query(value = "select history from users where id = :customerId", nativeQuery = true)
    Integer[] getHistory(@Param("customerId") int customerId);

    User findUserByEmail(String email);

    @Query(value = "select * from users where name = :username", nativeQuery = true)
    User findByName(@Param("username") String username);

    @Query(value = "SELECT users.name, users.phone_number, users.call, users.town, users.post, sc.products, sc.status\n" +
            "FROM users\n" +
            "JOIN public.shopping_cart sc ON sc.id = users.shopping_cart_id\n" +
            "WHERE sc.status <> 'delivered'\n" +
            "ORDER BY\n" +
            "  CASE\n" +
            "    WHEN sc.status = 'paid' THEN 0\n" +
            "    WHEN sc.status = 'created' THEN 1\n" +
            "    ELSE 2\n" +
            "  END;", nativeQuery = true)
    List<String> getOrders();
}
