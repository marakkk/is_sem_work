package org.marakobz.service;

import org.marakobz.dto.ReservationDto;
import org.marakobz.model.Reservation;
import org.marakobz.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public List<ReservationDto> getReservationsByUsername(String username) {
        List<Reservation> reservations = reservationRepository.findByUserUsername(username);
        return reservations.stream()
                .map(reservation -> {
                    ReservationDto dto = new ReservationDto();
                    dto.setReservationId(reservation.getId());
                    dto.setDreamId(reservation.getDream().getId());
                    dto.setCalendarId(reservation.getCalendar().getId());
                    dto.setUserId(reservation.getUser().getId());
                    dto.setStatus(reservation.getStatus().toString());
                    dto.setTimeOfReservation(reservation.getTimeOfReservation().toLocalDateTime());
                    return dto;
                })
                .collect(Collectors.toList());
    }
}

