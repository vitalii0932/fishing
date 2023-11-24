package com.example.ookp.service;

import com.example.ookp.dto.ShoppingCartDTO;
import com.example.ookp.mapper.ShoppingCartMapper;
import com.example.ookp.model.Product;
import com.example.ookp.model.ShoppingCart;
import com.example.ookp.repository.ProductRepository;
import com.example.ookp.repository.ShoppingCartRepository;
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
    private final ProductRepository productRepository;
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
            result.add(productRepository.findById(temp).get());
        }
        return result;
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Retryable(maxAttempts = 5)
    public void addProduct(int productId, int shoppingCartId) {
        var shoppingCart = shoppingCartRepository.findById(shoppingCartId).get();
        var price = shoppingCart.getTotalPrice();
        String stringProducts = shoppingCartRepository.getProducts(shoppingCartId);
        var array = stringProducts.split(",");
        int[] arrayProducts = new int[array.length + 1];
        Product product = productRepository.findById(productId).get();
        for(int i = 0; i < array.length; i++) {
            arrayProducts[i] = Integer.parseInt(array[i]);
        }
        arrayProducts[array.length] = productId;
        price += product.getPrice();
        shoppingCart.setProducts(arrayProducts);
        shoppingCart.setTotalPrice(price);
        shoppingCartRepository.save(shoppingCart);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Retryable(maxAttempts = 5)
    public void deleteProduct(int productId, int shoppingCartId) {
        System.out.println(productId);
        var shoppingCart = shoppingCartRepository.findById(shoppingCartId).get();
        var price = shoppingCart.getTotalPrice();

        String stringProducts = shoppingCartRepository.getProducts(shoppingCartId);
        var stringArray = stringProducts.split(",");
        List<Integer> intList = new ArrayList<>();
        for (var i = 0; i < stringArray.length; i++) {
            intList.add(Integer.valueOf(stringArray[i]));
        }

        int length = intList.size();
        if(length == 0) {
            System.out.println("Корзина порожня");
            return;
        }
        length--;

        Product product = productRepository.findById(productId).get();

        intList.remove(intList.indexOf(productId));
        int[] products = new int[length];
        for(int i = 0; i < length; i++) {
            products[i] = intList.get(i);
        }

        price -= product.getPrice();
        price = price < 0 ? 0 : price;

        shoppingCart.setProducts(products);
        System.out.println("1" + Arrays.toString(products));

        shoppingCart.setTotalPrice(price);
        shoppingCartRepository.save(shoppingCart);
    }

    @Transactional(readOnly = true)
    public double getTotalPrice(int id) {
        return shoppingCartRepository.getTotalPrice(id);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Retryable(maxAttempts = 5)
    public void editStatus(String userName) {
        var shoppingCart = shoppingCartRepository.getShoppingCartByUserName(userName);
        if(shoppingCart.getStatus().equals("paid")) {
            shoppingCart.setStatus("delivered");
        }
        shoppingCartRepository.save(shoppingCart);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Retryable(maxAttempts = 5)
    public int add(ShoppingCart shoppingCart) {
        shoppingCart.setStatus("paid");
        return shoppingCartRepository.save(shoppingCart).getId();
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Retryable(maxAttempts = 5)
    public int create() {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setProducts(new int[0]);
        shoppingCart.setTotalPrice(0);
        shoppingCart.setStatus("created");
        return shoppingCartRepository.save(shoppingCart).getId();
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Retryable(maxAttempts = 5)
    public void deleteById(int id) {
        shoppingCartRepository.deleteById(id);
    }
}
