package com.example.ookp.dto;

import lombok.Data;

import java.util.List;

@Data
public class ShoppingCartDTO {
    private int id;
    private int[] productsIds;
    private double totalPrice;
}
