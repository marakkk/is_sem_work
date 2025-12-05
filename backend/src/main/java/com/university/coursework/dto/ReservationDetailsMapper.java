package com.university.coursework.dto;

import com.university.coursework.model.Reservation;
import com.university.coursework.model.UsersDream;
import com.university.coursework.repository.UsersDreamRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.util.Optional;

@AllArgsConstructor
@Component
public class ReservationDetailsMapper {

    private final DreamMapper dreamMapper;

    private final ArchitectMapper architectMapper;

    private final CalendarMapper calendarMapper;

    private final UsersDreamRepository usersDreamRepository;

    private static final Logger logger = LoggerFactory.getLogger(ReservationDetailsMapper.class);

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

        logger.info("usersDreamsId: {}", usersDreamsId);


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

