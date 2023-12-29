package com.example.ookp.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

@Entity(name = "shopping_cart")
@Component
@Data
public class ShoppingCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private int[] products;
    @Column(name = "total_price")
    private Double totalPrice;
}
