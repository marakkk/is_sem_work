package org.marakobz.controller;

import org.marakobz.dto.CalendarDto;
import org.marakobz.dto.ReservationDto;
import org.marakobz.service.CalendarService;
import org.marakobz.service.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/dreams/home-page")
public class CustomerController {

    private final ReservationService reservationService;
    private final CalendarService calendarService;

    public CustomerController(ReservationService reservationService, CalendarService calendarService) {
        this.calendarService = calendarService;
        this.reservationService = reservationService;
    }

    @GetMapping("/reservations")
    public ResponseEntity<List<ReservationDto>> getReservations(Authentication authentication) {
        String username = authentication.getName();
        List<ReservationDto> reservations = reservationService.getReservationsByUsername(username);
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/calendar")
    public ResponseEntity<List<CalendarDto>> getCalendar() {
        List<CalendarDto> calendarEntries = calendarService.getCalendarEntries();
        return ResponseEntity.ok(calendarEntries);
    }
}
