package com.university.coursework.model;

import jakarta.persistence.*;
import lombok.Data;
import com.university.coursework.enums.CalendarStatus;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

@Data
@Entity
@Table(name = "calendar")
public class Calendar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "time", nullable = false)
    private LocalTime time;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CalendarStatus status;

    @OneToMany(mappedBy = "calendar", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Reservation> reservations;

}
