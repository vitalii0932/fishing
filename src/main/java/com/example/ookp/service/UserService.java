package com.example.ookp.service;

import com.example.ookp.dto.UserDTO;
import com.example.ookp.mapper.UserMapper;
import com.example.ookp.model.Role;
import com.example.ookp.model.ShoppingCart;
import com.example.ookp.repository.ProductRepository;
import com.example.ookp.repository.ShoppingCartRepository;
import com.example.ookp.repository.RoleRepository;
import com.example.ookp.repository.UserRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import com.example.ookp.model.User;
import org.springframework.context.annotation.Lazy;
import org.springframework.retry.annotation.Retryable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Data
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final ShoppingCartService shoppingCartService;
    private final OrderService orderService;
    private final ProductService productService;
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
        Integer[] history = user.getHistory() == null ? new Integer[1] : new Integer[user.getHistory().length + 1];
        for(int i = 0; i < history.length - 1; i++) {
            history[i] = user.getHistory()[i];
        }
        history[history.length - 1] = shoppingCartId;
        user.setHistory(history);
        user.setShoppingCart(shoppingCartService.findById(shoppingCartService.create()));
        userRepository.save(user);
        return user.getShoppingCart();
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Retryable(maxAttempts = 5)
    public User add(UserDTO userDTO) {
        User user = userMapper.toUser(userDTO);
        if(userDTO.getShoppingCartId() == 0) {
            user.setShoppingCart(null);
        } else {
            user.setShoppingCart(shoppingCartService.findById(userDTO.getShoppingCartId()));
        }
        Role role = roleService.findById(userDTO.getRoleId());
        user.setRole(role);
        return userRepository.save(user);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Retryable(maxAttempts = 5)
    public ShoppingCart buyProducts(User user, ShoppingCart shoppingCart) {
        productService.buyProducts(shoppingCart.getProducts());
        user.setRole(roleService.findById(1));
        user.setPost("Nova poshta " + user.getPost());
        var shoppingCartId = shoppingCartService.add(shoppingCart);
        var userTemp = userRepository.findUserByEmail(user.getEmail());
        if(userTemp == null) {
            userTemp = add(userMapper.toUserDTO(user));
        } else {
            userTemp.setEmail(user.getEmail());
            userTemp.setName(user.getName());
            userTemp.setCall(user.getCall());
            userTemp.setTown(user.getTown());
            userTemp.setPost(user.getPost());
            userTemp.setPhoneNumber(user.getPhoneNumber());
        }
        orderService.createNew(userTemp, shoppingCart);
        shoppingCart = addToHistory(userTemp.getId(), shoppingCartId);
        return shoppingCart;
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Retryable(maxAttempts = 5)
    public User registerUser(User user, ShoppingCart shoppingCart) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(roleService.findById(2));
        shoppingCart = shoppingCartService.findById(shoppingCartService.add(shoppingCart));
        user.setShoppingCart(shoppingCart);
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
    public User updateUserRole(int id) {
        var user = userMapper.toUserDTO(findById(id));
        if(findById(id).getRole().getId() == 3) {
            user.setRoleId(2);
        } else {
            user.setRoleId(3);
        }
        return add(user);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Retryable(maxAttempts = 5)
    public void deleteById(int id) {
        userRepository.deleteById(id);
    }
}
