package com.example.ookp.service;

import com.example.ookp.model.Order;
import com.example.ookp.model.ShoppingCart;
import com.example.ookp.model.User;
import com.example.ookp.repository.OrderRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Data
@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;

    @Transactional(readOnly = true)
    public List<Order> getAll() {
        return orderRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Order findById(int id) {
        return orderRepository.findById(id).get();
    }

    @Transactional(readOnly = true)
    public List<String> getOrdersByListString() {
        List<String> resultString = new ArrayList<>();
        var badList = orderRepository.getOrders();
        Boolean isArray = false;
        int size = badList.size();
        for(int i = 0; i < size; i++) {
            String str = "";
            String temp = badList.remove(0);
            var parts = temp.split(",");
            for (var part : parts) {
                if(!isArray) {
                    if(str.equals("")) {
                        str += part + ";";
                    } else {
                        if(part.matches("\\d+")) {
                            if(Integer.parseInt(part) > 100000000) {
                                str += part + ";";
                            } else {
                                isArray = true;
                                str += "[" + part;
                            }
                        } else {
                            str += part + ";";
                        }
                    }
                } else {
                    if(!part.matches("\\d+")) {
                        isArray = false;
                        str += "];" + part;
                    } else {
                        str += "," + part;
                    }
                }
                if(part.equals("created") || part.equals("send to customer") || part.equals("delivered")) {
                    resultString.add(str + " ");
                }
            }
        }
        System.out.println(resultString);
        return resultString;
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Retryable(maxAttempts = 5)
    public void editStatus(int id) {
        var order = orderRepository.findById(id).get();
        switch (order.getStatus()) {
            case "created":
                order.setStatus("send to customer");
                orderRepository.save(order);
                break;
            case "send to customer":
                order.setStatus("delivered");
                orderRepository.save(order);
                break;
            case "delivered":
                orderRepository.deleteById(id);
                break;
        }
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Retryable(maxAttempts = 5)
    public Order createNew(User user, ShoppingCart shoppingCart) {
        var order = new Order();
        order.setUser(user);
        order.setShoppingCart(shoppingCart);
        order.setStatus("created");
        return orderRepository.save(order);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Retryable(maxAttempts = 5)
    public void deleteById(int id) {
        orderRepository.deleteById(id);
    }
}
