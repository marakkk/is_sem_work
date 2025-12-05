package com.university.coursework.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import com.university.coursework.enums.AdminStatus;

@Data
@Entity
@Table(name = "admin")
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AdminStatus status;

    @ManyToOne
    @JoinColumn(name = "users_id", referencedColumnName = "id")
    @JsonBackReference("admin-dream_user")
    private DreamUser user;
}
