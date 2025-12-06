package com.coursework.model;

import jakarta.persistence.*;
import lombok.Data;
import com.coursework.enums.CharactersOccupation;
import com.coursework.enums.CharactersRelation;

@Data
@Entity
@Table(name = "characters")
public class Characters {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String characteristics;
    private String appearance;

    @Enumerated(EnumType.STRING)
    private CharactersRelation relation;

    @Enumerated(EnumType.STRING)
    private CharactersOccupation occupation;

}
