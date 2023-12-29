package com.example.ookp.dto;

import lombok.Data;

@Data
public class UserDTO {
    private int id;
    private String name;
    private String email;
    private String phoneNumber;
    private String password;
    private int roleId;
    private int shoppingCartId;
    private Integer[] history;
    private String town;
    private String post;
    private boolean call;
}
