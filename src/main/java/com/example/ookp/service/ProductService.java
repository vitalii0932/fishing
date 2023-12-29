package com.example.ookp.service;

import com.example.ookp.model.Product;
import com.example.ookp.repository.ProductRepository;
import com.example.ookp.dto.ProductDTO;
import com.example.ookp.mapper.ProductMapper;
import com.example.ookp.repository.TypeRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Data
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final TypeRepository typeRepository;
    private final ProductMapper productMapper;

    @Transactional(readOnly = true)
    public List<Product> getAll() {
        return productRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Product findById(int id) {
        return productRepository.findById(id).get();
    }

    @Transactional(readOnly = true)
    public List<Product> findByName(String name) {
        name = name.toLowerCase();
        name = name.substring(1, name.length() - 1);
        name = "%" + name + "%";
        return productRepository.findProductByName(name);
    }

    @Transactional(readOnly = true)
    public List<Product> findByCategoryId(int categoryId) {
        return productRepository.findProductsByTypeId(categoryId);
    }

    @Transactional(readOnly = true)
    public List<Product> findWithDiscount() {
        return productRepository.findProductsWithDiscount();
    }

    @Transactional(readOnly = true)
    public List<Product> findByTopSales(int count) {
        return productRepository.findTopBySalesProducts(count);
    }

    @Transactional(readOnly = true)
    public List<Product> getProductsByCategoryId(int categoryId) {
        return productRepository.getProductsByCategoryId(categoryId);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Retryable(maxAttempts = 5)
    public boolean buyProducts(Integer[] productsIds) {
        for(var item : productsIds) {
            var temp = findById(item);
            temp.setCount(temp.getCount() - 1);
            productRepository.save(temp);
        }
        return true;
    }

    @Transactional(readOnly = true)
    public List<Product> getProductsByIds(int[] ids) {
        List<Product> products = new ArrayList<>();
        if(ids == null) {
            return products;
        }
        for(var item : ids) {
            products.add(findById(item));
        }
        return products;
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Retryable(maxAttempts = 5)
    public void add(ProductDTO productDTO) {
        var product = productMapper.toProduct(productDTO);
        product.setType(typeRepository.findById(productDTO.getTypeId()).get());
        productRepository.save(product);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Retryable(maxAttempts = 5)
    public void deleteById(int id) {
        productRepository.deleteById(id);
    }
}
