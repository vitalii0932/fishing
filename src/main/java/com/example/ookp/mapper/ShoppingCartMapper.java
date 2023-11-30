package com.example.ookp.mapper;

import com.example.ookp.dto.ShoppingCartDTO;
import com.example.ookp.model.ShoppingCart;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@Component
@RequiredArgsConstructor
public class ShoppingCartMapper {

    public ShoppingCart toShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        var shoppingCart = new ShoppingCart();
        shoppingCart.setId(shoppingCartDTO.getId());
        shoppingCart.setProducts(shoppingCartDTO.getProductsIds());
        shoppingCart.setTotalPrice(shoppingCartDTO.getTotalPrice());
        return shoppingCart;
    }

    public ShoppingCartDTO toShoppingCartDTO(ShoppingCart shoppingCart) {
        var shoppingCartDTO = new ShoppingCartDTO();
        shoppingCartDTO.setId(shoppingCart.getId());
        shoppingCartDTO.setProductsIds(shoppingCart.getProducts());
        shoppingCartDTO.setTotalPrice(shoppingCart.getTotalPrice());
        return shoppingCartDTO;
    }
}
