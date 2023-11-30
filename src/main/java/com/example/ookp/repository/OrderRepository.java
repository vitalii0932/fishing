package com.example.ookp.repository;

import com.example.ookp.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    @Query(value = "select orders.id, u.name, u.phone_number, u.call, u.town, u.post, sc.products, status from orders \n" +
            "join public.users u on orders.user_id = u.id\n" +
            "join public.shopping_cart sc on sc.id = orders.shopping_cart_id\n", nativeQuery = true)
    List<String> getOrders();

    Order findByUserName(String userName);
}
