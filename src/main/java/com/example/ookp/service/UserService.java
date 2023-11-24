package com.example.ookp.service;

import com.example.ookp.dto.UserDTO;
import com.example.ookp.mapper.UserMapper;
import com.example.ookp.model.Role;
import com.example.ookp.model.ShoppingCart;
import com.example.ookp.repository.ProductRepository;
import com.example.ookp.repository.RoleRepository;
import com.example.ookp.repository.UserRepository;
import com.example.ookp.repository.ShoppingCartRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import com.example.ookp.model.User;
import org.springframework.retry.annotation.Retryable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@Data
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartService shoppingCartService;
    private final ProductRepository productRepository;
    private final UserMapper userMapper;

    @Transactional(readOnly = true)
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public User findById(int id) {
        return userRepository.findById(id).get();
    }

    @Transactional(readOnly = true)
    public User findByName(String name) {
        return userRepository.findByName(name);
    }

    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        System.out.println(userRepository.findUserByEmail(email));
        return userRepository.findUserByEmail(email);
    }

    @Transactional(readOnly = true)
    public ShoppingCart getShoppingCart(int id) {
        return userRepository.getShoppingCart(id);
    }

    @Transactional(readOnly = true)
    public List<ShoppingCart> getHistory(int id) {
        List<ShoppingCart> history = new ArrayList<>();
        if(userRepository.getHistory(id) != null) {
            for(var item : userRepository.getHistory(id)) {
                if(item != null) {
                    history.add(shoppingCartService.findById(item));
                }
            }
        }
        return history;
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Retryable(maxAttempts = 5)
    public ShoppingCart addToHistory(int userId, int shoppingCartId) {
        var user = findById(userId);
        var history = user.getHistory() == null ? new int[1] : new int[user.getHistory().length + 1];
        for(int i = 0; i < history.length - 1; i++) {
            history[i] = user.getHistory()[i];
        }
        history[history.length - 1] = shoppingCartId;
        user.setHistory(history);
        user.setShoppingCart(shoppingCartService.findById(shoppingCartService.create()));
        userRepository.save(user);
        return user.getShoppingCart();
    }

    @Transactional(readOnly = true)
    public List<String> getOrders() {
        List<String> resultString = new ArrayList<>();
        var badList = userRepository.getOrders();
        System.out.println(badList.toString());
        System.out.println(badList.size());
        int j = 0;


        Boolean isArray = false;
        int size = badList.size();
        for(int i = 0; i < size; i++) {
            String str = "";
            String temp = badList.remove(badList.size() - 1);
            var parts = temp.split(",");
            for (var part : parts) {
                if(!isArray) {
                    if(part.matches("\\d+")) {
                        if(Integer.parseInt(part) > 100) {
                            str += part + ";";
                        } else {
                            isArray = true;
                            str += "[" + part;
                        }
                    } else {
                        str += part + ";";
                    }
                } else {
                    if(!part.matches("\\d+")) {
                        isArray = false;
                        str += "];" + part;
                    } else {
                        str += "," + part;
                    }
                }
                if(part.equals("created") || part.equals("paid") || part.equals("delivered")) {
                    resultString.add(str + " ");
                }
            }
        }
        return resultString;
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Retryable(maxAttempts = 5)
    public User add(UserDTO userDTO) {
        User user = userMapper.toUser(userDTO);
        if(userDTO.getShoppingCartId() == 0) {
            user.setShoppingCart(null);
        } else {
            user.setShoppingCart(shoppingCartRepository.findById(userDTO.getShoppingCartId()).get());
        }
        Role role = roleRepository.findById(userDTO.getRoleId()).get();
        user.setRole(role);
        return userRepository.save(user);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Retryable(maxAttempts = 5)
    public User registerUser(UserDTO userDTO) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        var user = userMapper.toUser(userDTO);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setRole(roleRepository.findById(2).get());
        return userRepository.save(user);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Retryable(maxAttempts = 5)
    public User createUser() {
        User user = new User();
        Role role = new Role();
        role.setId(2);
        role.setName("USER");
        user.setRole(role);
        return userRepository.save(user);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Retryable(maxAttempts = 5)
    public void deleteById(int id) {
        userRepository.deleteById(id);
    }
}
