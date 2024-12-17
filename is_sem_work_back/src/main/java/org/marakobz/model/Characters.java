package org.marakobz.model;

import jakarta.persistence.*;
import lombok.Data;
import org.marakobz.enums.CharactersOccupation;
import org.marakobz.enums.CharactersRelation;
import java.util.Set;

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

    @ManyToMany(mappedBy = "characters")
    private Set<Dream> dreams;
}
