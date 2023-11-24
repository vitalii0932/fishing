package com.example.ookp.controller;

import com.example.ookp.dto.ProductDTO;
import com.example.ookp.dto.UserDTO;
import com.example.ookp.mapper.ProductMapper;
import com.example.ookp.mapper.UserMapper;
import com.example.ookp.model.Product;
import com.example.ookp.service.RoleService;
import com.example.ookp.service.UserService;
import com.example.ookp.service.ProductService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import com.example.ookp.model.User;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Data
@RestController
@RequestMapping("/adminData")
@RequiredArgsConstructor
public class AdminDataController {
    private final ProductService productService;
    private final ProductMapper productMapper;
    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping("/getTableData/products")
    public List<ProductDTO> getProducts() {
        var products = productService.getAll();
        List<ProductDTO> productsDTO = new ArrayList<>();
        for(var item : products) {
            var productDTO = productMapper.toProductDTO(item);
            productDTO.setTypeId(item.getType().getId());
            productsDTO.add(productDTO);
        }
        return productsDTO;
    }

    @GetMapping("/getTableData/users")
    public List<UserDTO> getCustomers() {
        var users = userService.getAll();
        List<UserDTO> usersDTO = new ArrayList<>();
        for(var item : users) {
            var userDTO = userMapper.toUserDTO(item);
            userDTO.setRoleId(item.getRole().getId());
            userDTO.setShoppingCartId(item.getShoppingCart() != null ? item.getShoppingCart().getId() : 0);
            usersDTO.add(userDTO);
        }
        return usersDTO;
    }

    @GetMapping("/getTableData/orders")
    public List<String> getOrders() {
        return userService.getOrders();
    }

    @PostMapping("/update")
    public void updateProducts(ProductDTO productDTO) {
        productService.add(productDTO);
    }
}
