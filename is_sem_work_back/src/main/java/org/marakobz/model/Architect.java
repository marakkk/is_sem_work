package org.marakobz.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "architect")
public class Architect {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "users_id", referencedColumnName = "id")
    private DreamUser user;

    @Column(name = "price", nullable = false)
    private int price;

    @Column(name = "rating")
    private int rating;


}
