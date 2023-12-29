package com.example.ookp.mapper;

import com.example.ookp.dto.UserDTO;
import com.example.ookp.model.User;
import com.example.ookp.service.RoleService;
import com.example.ookp.service.ShoppingCartService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@Component
@RequiredArgsConstructor
public class UserMapper {
    private final ShoppingCartService shoppingCartService;
    private final RoleService roleService;

    public User toUser(UserDTO userDTO) {
        var user = new User();
        user.setId(userDTO.getId());
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setPassword(userDTO.getPassword());
        if(userDTO.getShoppingCartId() == 0) {
            user.setShoppingCart(null);
        } else {
            user.setShoppingCart(shoppingCartService.findById(userDTO.getShoppingCartId()));
        }
        user.setHistory(userDTO.getHistory());
        if(userDTO.getRoleId() == 0) {
            user.setRole(null);
        } else {
            user.setRole(roleService.findById(userDTO.getRoleId()));
        }
        user.setTown(userDTO.getTown());
        user.setPost(userDTO.getPost());
        user.setCall(userDTO.isCall());
        return user;
    }

    public UserDTO toUserDTO(User user) {
        var userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setPhoneNumber(user.getPhoneNumber());
        userDTO.setPassword(user.getPassword());
        if(user.getShoppingCart() == null) {
            userDTO.setShoppingCartId(0);
        } else {
            userDTO.setShoppingCartId(user.getShoppingCart().getId());
        }
        userDTO.setHistory(user.getHistory());
        if(user.getRole() == null) {
            userDTO.setRoleId(0);
        } else {
            userDTO.setRoleId(user.getRole().getId());
        }
        userDTO.setTown(user.getTown());
        userDTO.setPost(user.getPost());
        userDTO.setCall(user.getCall());
        return userDTO;
    }
}
