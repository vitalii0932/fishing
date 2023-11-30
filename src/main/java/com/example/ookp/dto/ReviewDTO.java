package com.example.ookp.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ReviewDTO {
    private int id;
    private int userId;
    private String reviewText;
    private Date reviewDate;
}
