package com.example.ookp.controller;

import com.example.ookp.dto.UserDTO;
import com.example.ookp.mapper.ProductMapper;
import com.example.ookp.model.Product;
import com.example.ookp.model.ShoppingCart;
import com.example.ookp.model.User;
import com.example.ookp.repository.UserRepository;
import com.example.ookp.service.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/index")
@Data
@SessionAttributes(value = {"shoppingCart", "user"})
@RequiredArgsConstructor
public class MainController {
    private final ProductService productService;
    private final UserService userService;
    private final ProductMapper productMapper;
    private final ShoppingCartService shoppingCartService;
    private final TypeService typeService;
    private final CategoryService categoryService;

    @ModelAttribute("shoppingCart")
    public ShoppingCart createShoppingCart() {
        return new ShoppingCart();
    }

    @ModelAttribute("user")
    public User createUser() {
        return new User();
    }

    @GetMapping("")
    public String main(Model model, @ModelAttribute("shoppingCart") ShoppingCart shoppingCart) {
        model.addAttribute("title", "Fishing");
        var discountProducts = productService.findWithDiscount();
        model.addAttribute("discountProducts", discountProducts);
        var topProducts = productService.findByTopSales(15);
        model.addAttribute("topProducts", topProducts);
        return "index";
    }

    @GetMapping("/shop/{categoryId}")
    public String shopCategory(Model model, @PathVariable int categoryId, @ModelAttribute("shoppingCart") ShoppingCart shoppingCart) {
        var types = typeService.getTypesByCategoryId(categoryId);
        var category = categoryService.findById(categoryId);
        var products = productService.getProductsByCategoryId(categoryId);
        model.addAttribute("categoryName", category.getName());
        model.addAttribute("types", types);
        model.addAttribute("products", products);
        return "shop";
    }

    @GetMapping("/shop/type/{typeId}")
    public String shopType(Model model, @PathVariable int typeId, @ModelAttribute("shoppingCart") ShoppingCart shoppingCart) {
        var type = typeService.findById(typeId);
        var types = typeService.getTypesByCategoryId(type.getCategory().getId());
        var products = productService.findByCategoryId(typeId);
        model.addAttribute("categoryName", type.getName());
        model.addAttribute("types", types);
        model.addAttribute("products", products);
        return "shop";
    }

    @GetMapping("/shop/byName/{productName}")
    public String shopName(Model model, @PathVariable String productName, @ModelAttribute("shoppingCart") ShoppingCart shoppingCart) {
        var products = productService.findByName(productName);
        model.addAttribute("categoryName", "Результат за пошуком");
        model.addAttribute("products", products);
        return "shop";
    }

    @GetMapping("/shop/buy/{id}")
    public String buyProduct(Model model, @PathVariable int id, @ModelAttribute("shoppingCart") ShoppingCart shoppingCart) {
        System.out.println("buy");
        var products = shoppingCart.getProducts();
        var length = products == null ? 1 : products.length + 1;
        var price = shoppingCart.getTotalPrice();
        int[] newProducts = new int[length];
        if(products != null) {
            for(int i = 0; i < products.length; i++) {
                newProducts[i] = products[i];
            }
        }
        newProducts[length - 1] = id;
        price += productService.findById(id).getDiscount();
        shoppingCart.setProducts(newProducts);
        shoppingCart.setTotalPrice(price);
        return main(model, shoppingCart);
    }

    @GetMapping("/shoppingCart")
    public String shoppingCart(Model model, @ModelAttribute("shoppingCart") ShoppingCart shoppingCart) {
        System.out.println(shoppingCart);
        Product[] products = new Product[shoppingCart.getProducts().length];
        for(int i = 0; i < products.length; i++) {
            products[i] = productService.findById(shoppingCart.getProducts()[i]);
        }
        model.addAttribute("products", products);
        model.addAttribute("totalPrice", shoppingCart.getTotalPrice());
        return "cart";
    }

    @GetMapping("/shoppingCart/deleteProduct/{id}")
    public String deleteProduct(@PathVariable int id, @ModelAttribute("shoppingCart") ShoppingCart shoppingCart) {
        if(shoppingCart.getProducts() == null) {
            return "cart";
        }
        var product = productService.findById(id);
        var price = shoppingCart.getTotalPrice();
        int length = 0;
        if(shoppingCart.getProducts().length == 1) {
            shoppingCart.setProducts(null);
            shoppingCart.setTotalPrice(0);
            return "cart";
        }
        length = shoppingCart.getProducts().length - 1;
        List<Product> listProducts = new ArrayList<>();
        for(int i = 0; i < shoppingCart.getProducts().length; i++) {
            listProducts.add(productService.findById(shoppingCart.getProducts()[i]));
        }
        listProducts.remove(product);
        price -= product.getDiscount();
        int[] newProducts = new int[length];
        for(int i = 0; i < length; i++) {
            newProducts[i] = listProducts.get(i).getId();
        }
        shoppingCart.setProducts(newProducts);
        shoppingCart.setTotalPrice(price);
        return "cart";
    }

    @GetMapping("/buy")
    public String buyPage(Model model, @ModelAttribute("shoppingCart") ShoppingCart shoppingCart) {
        return "buy";
    }

    @PostMapping("/buy")
    public String buy(UserDTO userDTO, Model model, @ModelAttribute("shoppingCart") ShoppingCart shoppingCart) {
        productService.buyProducts(shoppingCart.getProducts());
        userDTO.setRoleId(1);
        var shoppingCartId = shoppingCartService.add(shoppingCart);
        var user = userService.add(userDTO);
        shoppingCart = userService.addToHistory(user.getId(), shoppingCartId);
        return main(model, shoppingCart);
    }

    @GetMapping("/registration")
    public String registration(Model model, @ModelAttribute("user") User user) {
        return "registration";
    }

    @PostMapping("/registration")
    public String registration(Model model, UserDTO userDTO, @ModelAttribute("user") User user) {
        user = userService.registerUser(userDTO);
        return "redirect::login";
    }

    @GetMapping("/user/")
    public String userPage(Model model, @ModelAttribute("shoppingCart") ShoppingCart shoppingCart, @ModelAttribute("user") User user, Principal principal) {
        user = userService.findByEmail(principal.getName());
        var userShoppingCart = user.getShoppingCart();
        shoppingCart.setId(userShoppingCart.getId());
        shoppingCart.setProducts(userShoppingCart.getProducts());
        shoppingCart.setTotalPrice(userShoppingCart.getTotalPrice());
        shoppingCart.setStatus(userShoppingCart.getStatus());
        model.addAttribute("name", user.getName());
        model.addAttribute("email", user.getEmail());
        model.addAttribute("phoneNumber", user.getPhoneNumber());
        model.addAttribute("products", userService.getHistory(user.getId()));
        return "user";
    }
}
