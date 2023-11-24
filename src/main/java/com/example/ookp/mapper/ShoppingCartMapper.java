package com.example.ookp.mapper;

import com.example.ookp.dto.ShoppingCartDTO;
import com.example.ookp.model.ShoppingCart;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ShoppingCartMapper {
    ShoppingCart toShoppingCart(ShoppingCartDTO shoppingCartDTO);
    ShoppingCartDTO toShoppingCartDTO(ShoppingCart shoppingCart);
}
