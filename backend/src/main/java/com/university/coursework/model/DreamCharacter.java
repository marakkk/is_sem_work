package com.university.coursework.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "dream_characters")
public class DreamCharacter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dreamCharactersId;

    @ManyToOne
    @JoinColumn(name = "characters_id")
    private Characters character;

    @ManyToOne
    @JoinColumn(name = "dream_id")
    private Dream dream;
}

