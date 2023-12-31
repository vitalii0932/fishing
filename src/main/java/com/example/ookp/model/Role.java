package com.example.ookp.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity(name = "roles")
@Data
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
}
