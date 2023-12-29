package com.example.ookp.controller;

import com.example.ookp.dto.ReviewDTO;
import com.example.ookp.dto.UserDTO;
import com.example.ookp.model.Product;
import com.example.ookp.model.ShoppingCart;
import com.example.ookp.model.User;
import com.example.ookp.service.*;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("")
@Data
@SessionAttributes(value = {"shoppingCart", "user"})
@RequiredArgsConstructor
public class MainController {
    private final ShoppingCartService shoppingCartService;
    private final ReviewService reviewService;
    private final CategoryService categoryService;
    private final ProductService productService;
    private final OrderService orderService;
    private final UserService userService;
    private final TypeService typeService;


    @ModelAttribute("shoppingCart")
    public ShoppingCart createShoppingCart() {
        var shoppingCart = new ShoppingCart();
        shoppingCart.setTotalPrice(0.0);
        return shoppingCart;
    }

    @ModelAttribute("user")
    public User createUser() {
        return new User();
    }

    @GetMapping("/")
    public String main(Model model, @ModelAttribute("shoppingCart") ShoppingCart shoppingCart) {
        var discountProducts = productService.findWithDiscount();
        var topProducts = productService.findByTopSales(15);
        var reviews = reviewService.getAll();
        model.addAttribute("title", "Fishing");
        model.addAttribute("discountProducts", discountProducts);
        model.addAttribute("topProducts", topProducts);
        model.addAttribute("reviews", reviews);
        return "index";
    }

    @GetMapping("/send")
    public String send(Model model) {
        return "send";
    }

    @GetMapping("/index/shop/{categoryId}")
    public String shopCategory(Model model, @PathVariable int categoryId, @ModelAttribute("shoppingCart") ShoppingCart shoppingCart) {
        var types = typeService.getTypesByCategoryId(categoryId);
        var category = categoryService.findById(categoryId);
        var products = productService.getProductsByCategoryId(categoryId);
        model.addAttribute("categoryName", category.getName());
        model.addAttribute("types", types);
        model.addAttribute("products", products);
        return "shop";
    }

    @GetMapping("/index/shop/type/{typeId}")
    public String shopType(Model model, @PathVariable int typeId, @ModelAttribute("shoppingCart") ShoppingCart shoppingCart) {
        var type = typeService.findById(typeId);
        var types = typeService.getTypesByCategoryId(type.getCategory().getId());
        var products = productService.findByCategoryId(typeId);
        model.addAttribute("categoryName", type.getName());
        model.addAttribute("types", types);
        model.addAttribute("products", products);
        return "shop";
    }

    @GetMapping("/index/shop/byName/{productName}")
    public String shopName(Model model, @PathVariable String productName, @ModelAttribute("shoppingCart") ShoppingCart shoppingCart) {
        var products = productService.findByName(productName);
        model.addAttribute("categoryName", "Результат за пошуком");
        model.addAttribute("products", products);
        return "shop";
    }

    @GetMapping("/index/shop/buy/{id}")
    public String buyProduct(Model model, @PathVariable int id, @ModelAttribute("shoppingCart") ShoppingCart shoppingCart) {
        shoppingCart = shoppingCartService.addProduct(id, shoppingCart);
        return main(model, shoppingCart);
    }

    @GetMapping("/index/shoppingCart")
    public String shoppingCart(Model model, @ModelAttribute("shoppingCart") ShoppingCart shoppingCart) {
        var products = shoppingCartService.getProductsByShoppingCart(shoppingCart);
        model.addAttribute("products", products);
        model.addAttribute("totalPrice", shoppingCart.getTotalPrice());
        return "cart";
    }

    @GetMapping("/index/shoppingCart/deleteProduct/{id}")
    public String deleteProduct(@PathVariable int id, @ModelAttribute("shoppingCart") ShoppingCart shoppingCart) {
        shoppingCart = shoppingCartService.deleteProduct(id, shoppingCart);
        return "cart";
    }

    @GetMapping("/index/buy")
    public String buyPage(Model model, @ModelAttribute("shoppingCart") ShoppingCart shoppingCart) {
        return "buy";
    }

    @PostMapping("/index/buy")
    public String buy(@Valid User user, Errors errors, Model model, @ModelAttribute("shoppingCart") ShoppingCart shoppingCart) {
        if(errors.hasErrors()) {
            var stringError = "";
            System.out.println(errors.getErrorCount());
            for(var error : errors.getFieldErrors()) {
                stringError +=  "\n" + error.getDefaultMessage();
            }
            model.addAttribute("message", stringError);
            return "buy";
        }

        var shoppingCartTemp = userService.buyProducts(user, shoppingCart);
        shoppingCart.setId(shoppingCartTemp.getId());
        shoppingCart.setProducts(shoppingCartTemp.getProducts());
        shoppingCart.setTotalPrice(shoppingCartTemp.getTotalPrice());
        return "redirect:/";
    }

    @GetMapping("/index/registration")
    public String registration(Model model) {
        return "registration";
    }

    @PostMapping("/index/user/registration")
    public String registration(Model model, @Valid User user, Errors errors, @ModelAttribute("shoppingCart") ShoppingCart shoppingCart) {
        if(errors.hasErrors()) {
            var stringError = "";
            System.out.println(errors.getErrorCount());
            for(var error : errors.getFieldErrors()) {
                stringError +=  "\n" + error.getDefaultMessage();
            }
            model.addAttribute("message", stringError);
            return "registration";
        }

        userService.registerUser(user, shoppingCart);
        return main(model, shoppingCart);
    }

    @GetMapping("/index/user/")
    public String userPage(Model model, @ModelAttribute("shoppingCart") ShoppingCart shoppingCart, @ModelAttribute("user") User user, Principal principal) {
        user.setId(userService.findByEmail(principal.getName()).getId());
        user.setName(userService.findByEmail(principal.getName()).getName());
        user.setEmail(userService.findByEmail(principal.getName()).getEmail());
        var userShoppingCart = user.getShoppingCart();
        if(userShoppingCart != null) {
            shoppingCart.setId(userShoppingCart.getId());
            shoppingCart.setProducts(userShoppingCart.getProducts());
            shoppingCart.setTotalPrice(userShoppingCart.getTotalPrice());
        }
        model.addAttribute("name", user.getName());
        model.addAttribute("email", user.getEmail());
        model.addAttribute("phoneNumber", user.getPhoneNumber());
        model.addAttribute("products", userService.getHistory(user.getId()));
        return "user";
    }

    @PostMapping("/index/submitReview")
    public String addReview(Model model, @ModelAttribute("user") User user, ReviewDTO reviewDTO) {
        reviewDTO.setUserId(user.getId());
        reviewService.add(reviewDTO);
        return "redirect:/index/user/";
    }
}
