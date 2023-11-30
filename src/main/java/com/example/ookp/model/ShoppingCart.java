package com.example.ookp.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.stereotype.Component;

@Entity(name = "shopping_cart")
@Component
@Data
public class ShoppingCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int[] products;
    @Column(name = "total_price")
    private double totalPrice;
}
