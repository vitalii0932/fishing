package com.example.ookp.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity(name = "type")
@Data
public class Type {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    @ManyToOne
    private Category category;
}
