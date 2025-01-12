package org.marakobz.dto;

import org.marakobz.model.Reservation;
import org.springframework.stereotype.Component;

@Component
public class ReservationDetailsMapper {

    private final DreamMapper dreamMapper;
    private final ArchitectMapper architectMapper;
    private final CalendarMapper calendarMapper;

    public ReservationDetailsMapper(DreamMapper dreamMapper, ArchitectMapper architectMapper, CalendarMapper calendarMapper) {
        this.dreamMapper = dreamMapper;
        this.architectMapper = architectMapper;
        this.calendarMapper = calendarMapper;
    }

    public ReservationDetailsDto toDetailsDto(Reservation reservation) {
        if (reservation == null) {
            throw new IllegalArgumentException("Reservation is null");
        }

        DreamDto dreamDto = dreamMapper.toDto(reservation.getDream());
        ArchitectDto architectDto = architectMapper.toDto(reservation.getArchitect());
        String formattedDate = calendarMapper.toFormattedDate(reservation.getCalendar());
        String formattedTime = calendarMapper.toFormattedTime(reservation.getCalendar());

        ReservationDetailsDto dto = new ReservationDetailsDto(
                reservation.getId(),
                dreamDto.getName(),
                dreamDto.getTimeEra(),
                dreamDto.getVirtualEnvironment(),
                dreamDto.getSpecialPowers(),
                dreamDto.getPhysicalRules(),
                dreamDto.getRole(),
                dreamDto.getGenre(),
                dreamDto.getScenario(),
                dreamDto.isTemplate(),
                dreamDto.getPrice(),
                formattedDate,
                formattedTime,
                reservation.getTimeOfReservation().toString(),
                architectDto.getUsername(),
                reservation.getStatus().toString(),
                dreamDto.getCharacters()
        );

        return dto;
    }
}

