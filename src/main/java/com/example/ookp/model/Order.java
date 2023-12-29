package com.example.ookp.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    private User user;
    @OneToOne
    private ShoppingCart shoppingCart;
    private String status;
}
