package com.example.ookp.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity(name = "reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    private User user;
    @Column(name = "review_text")
    private String reviewText;
    @Column(name = "review_date")
    private Date reviewDate;
}
