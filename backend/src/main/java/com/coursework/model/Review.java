package com.coursework.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "review")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "users_dreams_id")
    private UsersDream usersDream;

    private int mark;

    @ManyToOne
    @JoinColumn(name = "architect_id", nullable = false)
    private Architect architect;
}

