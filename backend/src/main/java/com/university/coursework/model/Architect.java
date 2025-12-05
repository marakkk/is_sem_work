package com.university.coursework.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import com.university.coursework.enums.AdminStatus;

@Data
@Entity
@Table(name = "architect")
public class Architect {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "users_id", referencedColumnName = "id")
    @JsonBackReference("architect-dream_user")
    private DreamUser user;

    @Column(name = "price", nullable = false)
    private int price;

    @Column(name = "rating")
    private int rating;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AdminStatus status;

}
