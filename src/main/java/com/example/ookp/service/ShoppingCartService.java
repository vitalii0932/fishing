package com.example.ookp.service;

import com.example.ookp.dto.UserDTO;
import com.example.ookp.mapper.ShoppingCartMapper;
import com.example.ookp.model.Product;
import com.example.ookp.model.ShoppingCart;
import com.example.ookp.repository.ProductRepository;
import com.example.ookp.repository.ShoppingCartRepository;
import com.example.ookp.repository.UserRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Data
@RequiredArgsConstructor
public class ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ProductService productService;
    private final OrderService orderService;
    private final ShoppingCartMapper shoppingCartMapper;

    @Transactional(readOnly = true)
    public List<ShoppingCart> getAll() {
        return shoppingCartRepository.findAll();
    }

    @Transactional(readOnly = true)
    public ShoppingCart findById(int id) {
        return shoppingCartRepository.findById(id).get();
    }

    @Transactional(readOnly = true)
    public List<Product> getProducts(int id) {
        var strArray = shoppingCartRepository.getProducts(id);
        var array = strArray.split(",");
        List<Product> result = new ArrayList<>();
        if(strArray.length() == 0) {
            return new ArrayList<>();
        }
        for(var item : array) {
            int temp = Integer.parseInt(item);
            result.add(productService.findById(temp));
        }
        return result;
    }

    @Transactional(readOnly = true)
    public List<Product> getProductsByShoppingCart(ShoppingCart shoppingCart) {
        List<Product> products = new ArrayList<>();
        if(shoppingCart.getProducts() != null) {
            for(var item : shoppingCart.getProducts()) {
                products.add(productService.findById(item));
            }
        }
        return products;
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Retryable(maxAttempts = 5)
    public ShoppingCart addProduct(int productId, ShoppingCart shoppingCart) {
        var products = shoppingCart.getProducts();
        var length = products == null ? 1 : products.length + 1;
        var cartTotalPrice = shoppingCart.getTotalPrice();
        int[] newProducts = new int[length];
        if(products != null) {
            for(int i = 0; i < products.length; i++) {
                newProducts[i] = products[i];
            }
        }
        newProducts[length - 1] = productId;
        cartTotalPrice += productService.findById(productId).getDiscount();
        shoppingCart.setProducts(newProducts);
        shoppingCart.setTotalPrice(cartTotalPrice);
        return shoppingCart;
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Retryable(maxAttempts = 5)
    public ShoppingCart deleteProduct(int productId, ShoppingCart shoppingCart) {
        if(shoppingCart.getProducts() != null) {
            var product = productService.findById(productId);
            var cartTotalPrice = shoppingCart.getTotalPrice();
            int length = shoppingCart.getProducts().length;
            if(length == 1) {
                shoppingCart.setProducts(null);
                shoppingCart.setTotalPrice(0);
            }
            List<Integer> listProducts = new ArrayList<>();
            for(int i = 0; i < length; i++) {
                listProducts.add(shoppingCart.getProducts()[i]);
            }
            listProducts.remove(listProducts.indexOf(productId));
            var newProducts = listProducts.stream()
                                            .mapToInt(Integer::intValue)
                                            .toArray();
            cartTotalPrice -= product.getPrice();
            shoppingCart.setProducts(newProducts);
            shoppingCart.setTotalPrice(cartTotalPrice);
        }
        return shoppingCart;
    }

    @Transactional(readOnly = true)
    public double getTotalPrice(int id) {
        return shoppingCartRepository.getTotalPrice(id);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Retryable(maxAttempts = 5)
    public int add(ShoppingCart shoppingCart) {
        return shoppingCartRepository.save(shoppingCart).getId();
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Retryable(maxAttempts = 5)
    public int create() {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setProducts(new int[0]);
        shoppingCart.setTotalPrice(0);
        return shoppingCartRepository.save(shoppingCart).getId();
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Retryable(maxAttempts = 5)
    public void deleteById(int id) {
        shoppingCartRepository.deleteById(id);
    }
}
