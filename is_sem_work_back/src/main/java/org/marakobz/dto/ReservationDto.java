package org.marakobz.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReservationDto {
    private Long reservationId;
    private Long userId;
    private Long dreamId;
    private Long calendarId;
    private LocalDateTime timeOfReservation;
    private String status;
}
