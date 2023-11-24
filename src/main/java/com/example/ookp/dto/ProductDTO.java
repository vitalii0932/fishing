package com.example.ookp.dto;

import lombok.Data;

@Data
public class ProductDTO {
    private int id;
    private String name;
    private String imagePath;
    private int typeId;
    private double price;
    private double discount;
    private int count;
    private int totalSales;
}
