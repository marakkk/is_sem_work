package com.university.coursework.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import com.university.coursework.enums.ReservationStatus;

import java.time.ZonedDateTime;

@Data
@Entity
@Table(name = "reservation")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "time_of_reservation", updatable = false)
    private ZonedDateTime timeOfReservation;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ReservationStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dream_id", nullable = false)
    @JsonBackReference("reservation-dream")
    private Dream dream;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id", nullable = false)
    @JsonBackReference("reservation-user")
    private DreamUser user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "calendar_id", nullable = false)
    @JsonBackReference("reservation-calendar")
    private Calendar calendar;

    @Column(name = "collective_partner")
    private Long collectivePartner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "architect_id", nullable = false)
    @JsonBackReference("reservation-architect")
    private Architect architect;

}
