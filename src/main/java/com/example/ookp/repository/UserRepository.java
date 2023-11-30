package com.example.ookp.repository;

import com.example.ookp.model.ShoppingCart;
import com.example.ookp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    @Query(value = "select shopping_cart_id from users where id = :customerId", nativeQuery = true)
    ShoppingCart getShoppingCart(@Param("customerId") int customerId);
    @Query(value = "select history from users where id = :customerId", nativeQuery = true)
    Integer[] getHistory(@Param("customerId") int customerId);

    User findUserByEmail(String email);

    @Query(value = "select * from users where name = :username", nativeQuery = true)
    User findByName(@Param("username") String username);
}
