package com.example.ookp.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity(name = "category")
@Data
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
}
