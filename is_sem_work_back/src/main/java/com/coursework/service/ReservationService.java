package com.coursework.service;

import com.coursework.dto.ReservationDetailsDto;
import com.coursework.dto.ReservationDetailsMapper;
import com.coursework.dto.ReservationDto;
import com.coursework.dto.ReservationTemplateDto;
import com.coursework.enums.*;
import com.coursework.model.*;
import com.coursework.repository.*;
import jakarta.persistence.NoResultException;
import jakarta.servlet.http.HttpServletRequest;
import com.coursework.security.JWTUtil;
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
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    public ReservationService(AuthService authService, ReservationDetailsMapper reservationMapper) {
        this.authService = authService;
        this.reservationMapper = reservationMapper;
    }

    @Transactional
    public Reservation createReservation(ReservationDto reservationDto, HttpServletRequest request) {
        try {
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


            Architect architect = architectRepository.findById(reservationDto.getArchitectId())
                    .orElseThrow(() -> new IllegalArgumentException("Architect not found"));

            if (!reservationDto.isTemplate()) {
                dream.setPrice(architect.getPrice()); // Use architect's price directly from the database
            } else {
                dream.setPrice((int) reservationDto.getPrice()); // Use DTO price for templates
            }

            dream.setCreator(creator);

            dream.setArchitect(architect);

            System.out.println("Reservation Architect ID: " + reservationDto.getArchitectId());

            UsersDream usersDream = new UsersDream();
            usersDream.setDream(dream);
            usersDream.setUser(creator);
            usersDreamRepository.save(usersDream);

            // Add Characters
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

            // Calendar Entry
            Calendar calendar = new Calendar();
            calendar.setTime(LocalTime.parse(reservationDto.getTime()));
            calendar.setDate(LocalDate.parse(reservationDto.getDate()));
            calendar.setStatus(CalendarStatus.NOT_AVAILABLE);
            calendarRepository.save(calendar);

            // Create Reservation
            Reservation reservation = new Reservation();
            reservation.setTimeOfReservation(ZonedDateTime.parse(reservationDto.getTimeOfReservation()));
            reservation.setStatus(ReservationStatus.CONFIRMED);
            reservation.setDream(dream);
            reservation.setUser(creator);
            reservation.setCalendar(calendar);
            reservation.setArchitect(architect);

            return reservationRepository.save(reservation);

        } catch (ResponseStatusException e) {
            throw e; // Rethrow to ensure proper HTTP response
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while creating the reservation", e);
        }
    }


    @Transactional
    public Reservation createTemplateReservation(ReservationTemplateDto templateDto, HttpServletRequest request) {
        try {
            String username = JWTUtil.extractUsernameFromRequest(request);
            if (username == null) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized access attempt");
            }

            DreamUser creator = authService.getUserByUsername(username);
            if (creator == null) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found");
            }


            Dream originalTemplate = dreamRepository.findById(templateDto.getOriginalTemplateId())
                    .orElseThrow(() -> new IllegalArgumentException("Original template not found"));

            Architect architect = architectRepository.findById(templateDto.getArchitectId())
                    .orElseThrow(() -> new IllegalArgumentException("Architect not found"));

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

            System.out.println("Creating template reservation for user " + reservation);
            return reservationRepository.save(reservation);


        } catch (ResponseStatusException e) {
            throw e; // Rethrow to ensure proper HTTP response
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while creating the template reservation", e);
        }
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


        // If the user has the role ARCHITECT, retrieve reservations by architect's id
        if (creator.getRole() == Roles.ARCHITECT) {
            // Retrieve the architect based on the user's ID (direct lookup from architect table)
            Architect architect = architectRepository.findByUserId(creator.getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Architect not found"));

            // Retrieve reservations by architect id
            reservations = reservationRepository.findReservationsByArchitectId(architect.getId());
        } else {
            reservations = reservationRepository.findReservationsByUserId(creator.getId());
        }

        if (reservations.isEmpty()) {
            return Collections.emptyList();
        }

        List<ReservationDetailsDto> reservationDetailsDtos = reservations.stream()
                .map(reservationMapper::toDetailsDto)
                .collect(Collectors.toList());


        return reservationDetailsDtos;
    }

    public List<Characters> getCharactersByReservationId(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Dream not found with ID: " + reservationId));
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
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Dream not found"));

        reservation.setStatus(ReservationStatus.DONE);
        return reservationRepository.save(reservation);
    }

    @Transactional
    public void updateResStatus(Long reservationId, ReservationStatus status) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new NoResultException("Reservation not found"));
        reservation.setStatus(status);
        reservationRepository.save(reservation);
    }
}
