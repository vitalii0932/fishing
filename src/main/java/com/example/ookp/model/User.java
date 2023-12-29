package com.example.ookp.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty(message = "Name should not be empty")
    @Size(min = 2, max = 30, message = "Name size should be between 2 and 30 characters")
    private String name;

    @NotEmpty(message = "Email should not be empty")
    @Email(message = "Email should be valid")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;
    private String password;
    @OneToOne
    private ShoppingCart shoppingCart;
    private Integer[] history;

    @ManyToOne
    private Role role;

    private String town;
    private String post;
    private Boolean call;
}
