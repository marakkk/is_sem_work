package org.marakobz.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.marakobz.dto.ReservationDetailsDto;
import org.marakobz.dto.ReservationDto;
import org.marakobz.dto.ReservationTemplateDto;
import org.marakobz.model.Characters;
import org.marakobz.model.Reservation;
import org.marakobz.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

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
            // Handles custom HTTP exceptions thrown from the service
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (IllegalArgumentException e) {
            // Handles validation or illegal argument exceptions
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            // Handles unexpected exceptions
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
            // Handles custom HTTP exceptions thrown from the service
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (IllegalArgumentException e) {
            // Handles validation or illegal argument exceptions
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            // Handles unexpected exceptions
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

}
