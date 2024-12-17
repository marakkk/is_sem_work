package org.marakobz.model;

import jakarta.persistence.*;
import lombok.Data;
import org.marakobz.enums.ReservationStatus;
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

    @ManyToOne
    @JoinColumn(name = "dream_id", nullable = false)
    private Dream dream;

    @ManyToOne
    @JoinColumn(name = "users_id", nullable = false)
    private DreamUser user;

    @ManyToOne
    @JoinColumn(name = "calendar_id", nullable = false)
    private Calendar calendar;
}
