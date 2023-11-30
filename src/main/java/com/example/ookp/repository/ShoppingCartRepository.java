package com.example.ookp.repository;

import com.example.ookp.model.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ShoppingCartRepository  extends JpaRepository<ShoppingCart, Integer> {
    @Query(value = "select id from shopping_cart order by id desc limit 1", nativeQuery = true)
    int getLastId();
    @Query(value = "select products from shopping_cart where id = :shoppingCartId", nativeQuery = true)
    String getProducts(@Param("shoppingCartId") int shoppingCartId);
    @Query(value = "select total_price from shopping_cart where id = :shoppingCartId", nativeQuery = true)
    double getTotalPrice(@Param("shoppingCartId") int shoppingCartId);

    @Query(value = "select shopping_cart.id, products, total_price, status from shopping_cart join public.users u on shopping_cart.id = u.shopping_cart_id where u.name = :userName", nativeQuery = true)
    ShoppingCart getShoppingCartByUserName(@Param("userName") String userName);
}
