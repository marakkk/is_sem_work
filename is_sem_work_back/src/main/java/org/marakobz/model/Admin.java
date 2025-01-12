package org.marakobz.model;

import jakarta.persistence.*;
import lombok.Data;
import org.marakobz.enums.AdminStatus;

import java.util.List;

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

}
