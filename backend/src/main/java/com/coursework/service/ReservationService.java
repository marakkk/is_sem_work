package com.coursework.service;

import com.coursework.dto.ReservationDetailsDto;
import com.coursework.dto.ReservationDetailsMapper;
import com.coursework.dto.ReservationDto;
import com.coursework.dto.ReservationTemplateDto;
import com.coursework.enums.*;
import com.coursework.model.*;
import com.coursework.repository.*;
import com.coursework.security.JWTUtil;
import jakarta.persistence.NoResultException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class ReservationService {

    @Autowired
    private DreamRepository dreamRepository;

    @Autowired
    private CharactersRepository characterRepository;

    @Autowired
    private ArchitectureRepository architectRepository;

    @Autowired
    private CalendarRepository calendarRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private UsersDreamRepository usersDreamRepository;

    private final AuthService authService;

    private final ReservationDetailsMapper reservationMapper;

    private static final Logger logger = LoggerFactory.getLogger(ReservationService.class);

    @Transactional
    public Reservation createReservation(ReservationDto reservationDto, HttpServletRequest request) {
        String username = JWTUtil.extractUsernameFromRequest(request);
        if (username == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized access attempt");
        }

        DreamUser creator = authService.getUserByUsername(username);
        if (creator == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found");
        }

        Dream dream = new Dream();
        dream.setName(reservationDto.getDreamName());
        dream.setTimeEra(DreamTimeEra.valueOf(reservationDto.getTimeEra()));
        dream.setVirtualEnvironment(DreamVirtualEnvironment.valueOf(reservationDto.getVirtualEnvironment()));
        dream.setSpecialPowers(DreamSpecialPowers.valueOf(reservationDto.getSpecialPowers()));
        dream.setPhysicalRules(DreamPhysicalRules.valueOf(reservationDto.getPhysicalRules()));
        dream.setRole(DreamRole.valueOf(reservationDto.getRole()));
        dream.setGenre(DreamGenre.valueOf(reservationDto.getGenre()));
        dream.setScenario(reservationDto.getScenario());
        dream.setTemplate(reservationDto.isTemplate());

        Architect architect = architectRepository.findById(reservationDto.getArchitectId()).orElseThrow(() -> new IllegalArgumentException("Architect not found"));

        if (!reservationDto.isTemplate()) {
            dream.setPrice(architect.getPrice());
        } else {
            dream.setPrice((int) reservationDto.getPrice());
        }

        dream.setCreator(creator);
        dream.setArchitect(architect);

        logger.info("Reservation Architect ID: {}", reservationDto.getArchitectId());

        UsersDream usersDream = new UsersDream();
        usersDream.setDream(dream);
        usersDream.setUser(creator);
        usersDreamRepository.save(usersDream);

        Set<Characters> characters = reservationDto.getCharacters().stream().map(characterDto -> {
            Characters character = new Characters();
            character.setName(characterDto.getName());
            character.setCharacteristics(characterDto.getCharacteristics());
            character.setAppearance(characterDto.getAppearance());
            character.setRelation(CharactersRelation.valueOf(characterDto.getRelation()));
            character.setOccupation(CharactersOccupation.valueOf(characterDto.getOccupation()));
            return characterRepository.save(character);
        }).collect(Collectors.toSet());
        dream.setCharacters(characters);

        dreamRepository.save(dream);

        Calendar calendar = new Calendar();
        calendar.setTime(LocalTime.parse(reservationDto.getTime()));
        calendar.setDate(LocalDate.parse(reservationDto.getDate()));
        calendar.setStatus(CalendarStatus.NOT_AVAILABLE);
        calendarRepository.save(calendar);

        Reservation reservation = new Reservation();
        reservation.setTimeOfReservation(ZonedDateTime.parse(reservationDto.getTimeOfReservation()));
        reservation.setStatus(ReservationStatus.CONFIRMED);
        reservation.setDream(dream);
        reservation.setUser(creator);
        reservation.setCalendar(calendar);
        reservation.setArchitect(architect);
        reservation.setCollectivePartner(reservationDto.getCollectivePartner());

        return reservationRepository.save(reservation);
    }

    @Transactional
    public Reservation createTemplateReservation(ReservationTemplateDto templateDto, HttpServletRequest request) {
        String username = JWTUtil.extractUsernameFromRequest(request);
        if (username == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized access attempt");
        }

        DreamUser creator = authService.getUserByUsername(username);
        if (creator == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found");
        }

        Dream originalTemplate = dreamRepository.findById(templateDto.getOriginalTemplateId()).orElseThrow(() -> new IllegalArgumentException("Original template not found"));

        Architect architect = architectRepository.findById(templateDto.getArchitectId()).orElseThrow(() -> new IllegalArgumentException("Architect not found"));

        Calendar calendar = new Calendar();
        calendar.setDate(LocalDate.parse(templateDto.getDate()));
        calendar.setTime(LocalTime.parse(templateDto.getTime()));
        calendar.setStatus(CalendarStatus.NOT_AVAILABLE);
        calendarRepository.save(calendar);

        UsersDream usersDream = new UsersDream();
        usersDream.setDream(originalTemplate);
        usersDreamRepository.save(usersDream);

        Reservation reservation = new Reservation();
        reservation.setTimeOfReservation(ZonedDateTime.parse(templateDto.getTimeOfReservation()));
        reservation.setStatus(ReservationStatus.CONFIRMED);
        reservation.setDream(originalTemplate);
        reservation.setUser(creator);
        reservation.setCalendar(calendar);
        reservation.setArchitect(architect);

        logger.info("Creating template reservation for user {}", reservation);
        return reservationRepository.save(reservation);
    }

    @Transactional
    public List<ReservationDetailsDto> getReservations(HttpServletRequest request) {
        String username = JWTUtil.extractUsernameFromRequest(request);

        if (username == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized access attempt");
        }

        DreamUser creator = authService.getUserByUsername(username);
        if (creator == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found");
        }

        List<Reservation> reservations;

        if (creator.getRole() == Roles.ARCHITECT) {
            Architect architect = architectRepository.findByUserId(creator.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Architect not found"));

            reservations = reservationRepository.findReservationsByArchitectId(architect.getId());
        } else {
            reservations = reservationRepository.findReservationsByUserId(creator.getId());
        }

        return reservations.stream().map(reservationMapper::toDetailsDto).collect(Collectors.toList());
    }

    public List<Characters> getCharactersByReservationId(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(() -> new RuntimeException("Dream not found with ID: " + reservationId));
        return new ArrayList<>(reservation.getDream().getCharacters());
    }

    @Transactional
    public List<Calendar> getCalendarEntries(String date, String time, String calendarStatus) {
        LocalDate parsedDate = LocalDate.parse(date);
        LocalTime parsedTime = LocalTime.parse(time);

        if (calendarStatus != null) {
            CalendarStatus status = CalendarStatus.valueOf(calendarStatus);
            return calendarRepository.findByDateAndTimeAndStatus(parsedDate, parsedTime, status);
        } else {
            return calendarRepository.findByDateAndTime(parsedDate, parsedTime);
        }
    }

    @Transactional
    public Reservation updateReservationStatus(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(() -> new IllegalArgumentException("Dream not found"));
        reservation.setStatus(ReservationStatus.DONE);
        return reservationRepository.save(reservation);
    }

    @Transactional
    public void updateResStatus(Long reservationId, ReservationStatus status) {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(() -> new NoResultException("Reservation not found"));
        reservation.setStatus(status);
        reservationRepository.save(reservation);
    }
}
