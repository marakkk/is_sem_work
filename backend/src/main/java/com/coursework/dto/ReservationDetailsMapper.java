package com.coursework.dto;

import com.coursework.model.Reservation;
import com.coursework.model.UsersDream;
import com.coursework.repository.UsersDreamRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ReservationDetailsMapper {

    private final DreamMapper dreamMapper;
    private final ArchitectMapper architectMapper;
    private final CalendarMapper calendarMapper;
    private final UsersDreamRepository usersDreamRepository;

    public ReservationDetailsMapper(DreamMapper dreamMapper, ArchitectMapper architectMapper, CalendarMapper calendarMapper, UsersDreamRepository usersDreamRepository) {
        this.dreamMapper = dreamMapper;
        this.architectMapper = architectMapper;
        this.calendarMapper = calendarMapper;
        this.usersDreamRepository = usersDreamRepository;
    }

    public ReservationDetailsDto toDetailsDto(Reservation reservation) {
        if (reservation == null) {
            throw new IllegalArgumentException("Reservation is null");
        }

        DreamDto dreamDto = dreamMapper.toDto(reservation.getDream());
        ArchitectDto architectDto = architectMapper.toDto(reservation.getArchitect());
        String formattedDate = calendarMapper.toFormattedDate(reservation.getCalendar());
        String formattedTime = calendarMapper.toFormattedTime(reservation.getCalendar());
        String formattedCalendarStatus = calendarMapper.toStatus(reservation.getCalendar());

        Long usersDreamsId = null;
        Optional<UsersDream> usersDream = usersDreamRepository.findByDream(reservation.getDream());
            if (usersDream.isPresent()) {
                usersDreamsId = usersDream.get().getUsersDreamsId();
            }
        System.out.println("usersDreamsId: " + usersDreamsId);


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
                formattedCalendarStatus,
                reservation.getTimeOfReservation().toString(),
                architectDto.getUsername(),
                reservation.getStatus().getReservationStatus(),
                dreamDto.getCharacters(),
                usersDreamsId,
                reservation.getArchitect().getId()

        );

        return dto;
    }
}

