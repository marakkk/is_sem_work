package org.marakobz.model;

import jakarta.persistence.*;
import java.util.Set;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.marakobz.enums.*;

@Data
@Entity
@Table(name = "dream")
public class Dream {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 500)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "time_era", nullable = false)
    private DreamTimeEra timeEra;

    @Enumerated(EnumType.STRING)
    @Column(name = "virtual_environment", nullable = false)
    private DreamVirtualEnvironment virtualEnvironment;

    @Enumerated(EnumType.STRING)
    @Column(name = "special_powers")
    private DreamSpecialPowers specialPowers;

    @Enumerated(EnumType.STRING)
    @Column(name = "physical_rules")
    private DreamPhysicalRules physicalRules;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private DreamRole role;

    @Column(name = "scenario")
    private String scenario;

    @Column(name = "template")
    private boolean template;

    @Enumerated(EnumType.STRING)
    @Column(name = "genre", nullable = false)
    private DreamGenre genre;

    @Column(name = "price")
    private int price;

    @ManyToMany
    @JoinTable(
            name = "dream_characters",
            joinColumns = @JoinColumn(name = "dream_id"),
            inverseJoinColumns = @JoinColumn(name = "characters_id")
    )
    private Set<Characters> characters;

    @ManyToOne
    @JoinColumn(name = "architect_id")
    private Architect architect;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private DreamUser creator;
}
