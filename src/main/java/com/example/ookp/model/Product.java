package com.example.ookp.model;

import lombok.Data;

import jakarta.persistence.*;

@Entity(name = "products")
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    @Column(name = "image_path")
    private String imagePath;
    @ManyToOne
    private Type type;
    private double price;
    private double discount;
    private int count;
    @Column(name = "total_sales")
    private int totalSales;
}
