package org.marakobz.service;

import jakarta.servlet.http.HttpServletRequest;
import org.marakobz.dto.ReservationDetailsDto;
import org.marakobz.dto.ReservationDto;
import org.marakobz.dto.ReservationDetailsMapper;
import org.marakobz.dto.ReservationTemplateDto;
import org.marakobz.enums.*;
import org.marakobz.model.*;
import org.marakobz.repository.*;
import org.marakobz.security.JWTUtil;
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
    private UserRepository userRepository;

    private final AuthService authService;
    private final ReservationDetailsMapper reservationMapper;

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

            System.out.println("Creating reservation for user " + username);

            // Create and save Dream
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

            System.out.println("Reservation DTO: " + reservationDto);


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

            System.out.println("Creating template reservation for user " + username);

            // Validate the template and architect
            Dream originalTemplate = dreamRepository.findById(templateDto.getOriginalTemplateId())
                    .orElseThrow(() -> new IllegalArgumentException("Original template not found"));

            Architect architect = architectRepository.findById(templateDto.getArchitectId())
                    .orElseThrow(() -> new IllegalArgumentException("Architect not found"));

            // Create a new calendar entry
            Calendar calendar = new Calendar();
            calendar.setDate(LocalDate.parse(templateDto.getDate()));
            calendar.setTime(LocalTime.parse(templateDto.getTime()));
            calendar.setStatus(CalendarStatus.NOT_AVAILABLE);
            calendarRepository.save(calendar);

            // Create the reservation
            Reservation reservation = new Reservation();
            reservation.setTimeOfReservation(ZonedDateTime.parse(templateDto.getTimeOfReservation()));
            reservation.setStatus(ReservationStatus.CONFIRMED);
            reservation.setDream(originalTemplate); // Associate the original template
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
            // Otherwise, retrieve reservations by user id
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


}
