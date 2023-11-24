package com.example.ookp.controller;

import com.example.ookp.dto.ProductDTO;
import com.example.ookp.mapper.ProductMapper;
import com.example.ookp.mapper.UserMapper;
import com.example.ookp.model.Product;
import com.example.ookp.model.Type;
import com.example.ookp.service.ProductService;
import com.example.ookp.service.ShoppingCartService;
import com.example.ookp.service.TypeService;
import com.example.ookp.service.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Data
@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminPageController {
    private final ProductService productService;
    private final ShoppingCartService shoppingCartService;
    private final UserService userService;
    private final TypeService typeService;
    private final ProductMapper productMapper;
    private final UserMapper userMapper;

    @GetMapping("/")
    public String admin(Model model) {
        model.addAttribute("title", "admin page");
        return "admin";
    }

    @GetMapping("/update/products/{id}")
    public String updateProduct(Model model, @PathVariable("id") int id) {
        var product = productService.findById(id);
        model.addAttribute("title", "Update product");
        model.addAttribute("product", product);
        return "update";
    }

    @GetMapping("/update/products/add")
    public String addProduct(Model model) {
        var product = new Product();
        product.setType(new Type());
        model.addAttribute("title", "Add product");
        model.addAttribute("product", product);
        return "update";
    }

    @GetMapping("/update/users/{id}")
    public String updateUser(Model model, @PathVariable("id") int id) {
        var user = userMapper.toUserDTO(userService.findById(id));
        if(userService.findById(id).getRole().getId() == 3) {
            user.setRoleId(2);
        } else {
            user.setRoleId(3);
        }
        userService.add(user);
        return "admin";
    }

    @PostMapping("/update")
    public String update(Model model, ProductDTO productDTO) {
        productService.add(productDTO);
        return admin(model);
    }

    @GetMapping("/delete/products/{id}")
    public String deleteProduct(Model model, @PathVariable("id") int id) {
        productService.deleteById(id);
        return admin(model);
    }

    @GetMapping("/delete/users/{id}")
    public String deleteUser(Model model, @PathVariable("id") int id) {
        userService.deleteById(id);
        return admin(model);
    }

    @GetMapping("/editStatus/{userName}")
    public String editStatus(Model model, @PathVariable("userName") String userName) {
        shoppingCartService.editStatus(userName);
        return admin(model);
    }
}
