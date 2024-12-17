package org.marakobz.service;

import org.marakobz.dto.DreamDto;
import org.marakobz.model.*;
import org.marakobz.repository.DreamRepository;
import org.marakobz.repository.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Service
public class DreamService {

    private final DreamRepository dreamRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private static final Logger logger = LoggerFactory.getLogger(DreamService.class);

    public DreamService(DreamRepository dreamRepository, UserRepository userRepository, AuthService authService) {
        this.dreamRepository = dreamRepository;
        this.userRepository = userRepository;
        this.authService = authService;
    }

    @Transactional
    public void approveUser(DreamUser user) {
        DreamUser existingUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        userRepository.save(existingUser);
    }

    @Transactional(readOnly = true)
    public Page<DreamDto> getAllCreatures(Pageable pageable) {
        return dreamRepository.findAll(pageable)
                .map(this::convertToDto);
    }


   /* @Transactional
    public void createDream(Dream dream, HttpServletRequest request) {
        String username = JWTUtil.extractUsernameFromRequest(request);
        if (username == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized access attempt");
        }
        User creator = authService.getUserByUsername(username);
        if (creator == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found");
        }

        if (dreamRepository.existsByNameAndCreator(dream.getName().trim().toLowerCase(), creator)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Dream с именем '" + dream.getName() + "' уже существует для этого пользователя.");
        }

        dream.setCreator(creator);
        dreamRepository.save(dream);
    }*/

/*    @Transactional
    public Dream updateDream(Long id, DreamDto updatedDream, HttpServletRequest request) {
        Dream existingDream = dreamRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Dream not found"));

        String username = JWTUtil.extractUsernameFromRequest(request);
        User user = authService.getUserByUsername(username);

        boolean isCreator = existingDream.getCreator().getUsername().equals(user.getUsername());
        boolean hasApprovalToEdit = user.isApproved() && Boolean.TRUE.equals(existingDream.isApproveUpdates());

        if (!isCreator && !hasApprovalToEdit) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User not allowed to update this creature");
        }
*//*
        existingCreature.setName(updatedCreature.getName());
        existingCreature.setAge(updatedCreature.getAge());
        existingCreature.setCoordinates(updatedCreature.getCoordinates());
        existingCreature.setCreatureType(updatedCreature.getCreatureType());
        existingCreature.setCreatureLocation(updatedCreature.getCreatureLocation());
        existingCreature.setAttackLevel(updatedCreature.getAttackLevel());
        existingCreature.setDefenseLevel(updatedCreature.getDefenseLevel());
        existingCreature.setRing(updatedCreature.getRing());
        existingCreature.setApproveUpdates(updatedCreature.isApproveUpdates());*//*

        return dreamRepository.save(existingDream);
    }*/


    /*@Transactional
    public void deleteDreamCreature(Long id, HttpServletRequest request) {
        if (!dreamRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Creature not found");
        }

        dreamRepository.deleteByIdWithCascade(id);
    }*/



/*    @Transactional(readOnly = true)
    public Page<DreamDto> findByFilters(String username, String name, Double x, Double y, Long age, BookCreatureType creatureType, String locationName, Float area,
                                        Long population, BookCreatureType governor, Double populationDensity,
                                        Float attackLevel, Double defenseLevel, String ringName,
                                        Float ringPower, Float ringWeight,
                                        int page, int size, String sortField, Sort.Direction sortDirection) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortField));

        Page<Dream> creatures = dreamRepository.findByFilters(username, name, x, y, age, creatureType, locationName, area, population, governor,
                populationDensity, attackLevel, defenseLevel, ringName, ringPower, ringWeight, pageable);

        return creatures.map(this::convertToDto);
    }*/


    private DreamDto convertToDto(Dream dream) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm:ss").withZone(ZoneId.of("Europe/Moscow"));


        DreamDto dto = new DreamDto();
        /*dto.setId(creature.getId());
        dto.setName(creature.getName());
        dto.setAge(creature.getAge());
        dto.setCoordinates(creature.getCoordinates());
        dto.setCreationDate(formatter.format(creature.getCreationDate()));
        dto.setCreatureType(creature.getCreatureType());
        dto.setCreatureLocation(creature.getCreatureLocation());
        dto.setAttackLevel(creature.getAttackLevel());
        dto.setDefenseLevel(creature.getDefenseLevel());
        dto.setRing(creature.getRing());
        dto.setCreatorName(creature.getCreator() != null ? creature.getCreator().getUsername() : null);
        dto.setApproveUpdates(creature.isApproveUpdates());
        logger.info("Mapping creature {} with creator {}",
                creature.getName(),
                creature.getCreator() != null ? creature.getCreator().getUsername() : "null");*/

        return dto;
    }

}
