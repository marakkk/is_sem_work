package com.coursework.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "users_dreams")
public class UsersDream {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long usersDreamsId;

    @ManyToOne
    @JoinColumn(name = "users_id")
    private DreamUser user;

    @ManyToOne
    @JoinColumn(name = "dream_id")
    private Dream dream;
}

