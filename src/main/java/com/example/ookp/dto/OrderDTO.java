package com.example.ookp.dto;

import lombok.Data;

@Data
public class OrderDTO {
    private int id;
    private int userId;
    private int shoppingCartId;
    private String status;
}
