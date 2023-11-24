package com.example.ookp.model;

import lombok.Data;
import jakarta.persistence.*;

@Entity(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String email;
    @Column(name = "phone_number")
    private String phoneNumber;
    private String password;
    @OneToOne
    private ShoppingCart shoppingCart;
    private int[] history;

    @ManyToOne
    private Role role;

    private String town;
    private String post;
    private boolean call;
}
