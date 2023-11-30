package com.example.ookp.mapper;

import com.example.ookp.dto.OrderDTO;
import com.example.ookp.model.Order;
import com.example.ookp.service.ShoppingCartService;
import com.example.ookp.service.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@Component
@RequiredArgsConstructor
public class OrderMapper {
    private final UserService userService;
    private final ShoppingCartService shoppingCartService;

    public Order toOrder(OrderDTO orderDTO) {
        var order = new Order();
        order.setId(orderDTO.getId());
        if(orderDTO.getUserId() == 0) {
            order.setUser(null);
        } else {
            order.setUser(userService.findById(orderDTO.getUserId()));
        }
        if(orderDTO.getShoppingCartId() == 0) {
            order.setShoppingCart(null);
        } else {
            order.setShoppingCart(shoppingCartService.findById(orderDTO.getShoppingCartId()));
        }
        order.setStatus(orderDTO.getStatus());
        return order;
    }

    public OrderDTO toOrderDTO(Order order) {
        var orderDTO = new OrderDTO();
        orderDTO.setId(order.getId());
        if(order.getUser() == null) {
            orderDTO.setUserId(0);
        } else {
            orderDTO.setUserId(order.getUser().getId());
        }
        if(order.getShoppingCart() == null) {
            orderDTO.setShoppingCartId(0);
        } else {
            orderDTO.setShoppingCartId(order.getShoppingCart().getId());
        }
        orderDTO.setStatus(order.getStatus());
        return orderDTO;
    }
}
