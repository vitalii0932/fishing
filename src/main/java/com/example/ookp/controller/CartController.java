package com.example.ookp.controller;

import com.example.ookp.service.ShoppingCartService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Data
@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {
    private final ShoppingCartService shoppingCartService;

    @GetMapping("/{shoppingCartId}")
    public String cartPage(Model model, @PathVariable("shoppingCartId") int shoppingCartId) {
        var shoppingCart = shoppingCartService.findById(shoppingCartId);
        var products = shoppingCartService.getProducts(shoppingCartId);
        model.addAttribute("title", shoppingCartId);
        model.addAttribute("products", products);
        model.addAttribute("totalPrice", shoppingCart.getTotalPrice());
        return "cart";
    }
}
