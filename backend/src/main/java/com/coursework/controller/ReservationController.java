package com.coursework.controller;

import com.coursework.dto.CalendarDto;
import com.coursework.dto.ReservationDetailsDto;
import com.coursework.dto.ReservationDto;
import com.coursework.dto.ReservationTemplateDto;
import com.coursework.model.Calendar;
import com.coursework.model.Characters;
import com.coursework.model.Reservation;
import com.coursework.service.ReservationService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @PostMapping("/confirm")
    public ResponseEntity<?> createReservation(@RequestBody ReservationDto reservationDto, HttpServletRequest request) {
        try {
            Reservation reservation = reservationService.createReservation(reservationDto, request);
            return ResponseEntity.status(HttpStatus.CREATED).body(reservation);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred while creating the reservation.");
        }
    }

    @PostMapping("/confirm-template")
    public ResponseEntity<?> createTemplateReservation(@RequestBody ReservationTemplateDto templateDto, HttpServletRequest request) {
        try {
            Reservation reservation = reservationService.createTemplateReservation(templateDto, request);
            return ResponseEntity.status(HttpStatus.CREATED).body(reservation);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred while creating the template reservation.");
        }
    }

    @GetMapping("/history")
    public List<ReservationDetailsDto> getReservations(HttpServletRequest request) {
        return reservationService.getReservations(request);
    }

    @GetMapping("/{reservationId}/characters")
    public ResponseEntity<List<Characters>> getCharactersByReservationId(@PathVariable Long reservationId) {
        List<Characters> characters = reservationService.getCharactersByReservationId(reservationId);
        return ResponseEntity.ok(characters);
    }

    @GetMapping("/calendar-entries")
    public ResponseEntity<List<CalendarDto>> getCalendarEntries(
            @RequestParam String date,
            @RequestParam String time,
            @RequestParam(required = false) String status
    ) {
        try {
            List<Calendar> calendarEntries = reservationService.getCalendarEntries(date, time, status);
            List<CalendarDto> response = calendarEntries.stream()
                    .map(entry -> new CalendarDto(
                            entry.getId(),
                            entry.getDate(),
                            entry.getTime(),
                            entry.getStatus()
                    ))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }

    @PutMapping("/update-status/{reservationId}")
    public ResponseEntity<Reservation> updateReservationStatus(@PathVariable Long reservationId) {
        Reservation updatedReservation = reservationService.updateReservationStatus(reservationId);
        return ResponseEntity.ok(updatedReservation);
    }
}
