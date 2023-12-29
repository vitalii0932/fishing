package com.example.ookp.model;

import lombok.Data;

import jakarta.persistence.*;

@Entity(name = "products")
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    @Column(name = "image_path")
    private String imagePath;
    @ManyToOne
    private Type type;
    private Double price;
    private Double discount;
    private Integer count;
    @Column(name = "total_sales")
    private Integer totalSales;
}
