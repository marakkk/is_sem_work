package com.university.coursework.service;

import com.university.coursework.enums.*;
import com.university.coursework.model.*;
import com.university.coursework.repository.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import com.university.coursework.dto.ArchitectDto;
import com.university.coursework.dto.DreamDto;
import lombok.AllArgsConstructor;
import com.university.coursework.security.JWTUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;


@AllArgsConstructor
@Service
public class ArchitectService {

    @Autowired
    private final DreamRepository dreamRepository;

    @Autowired
    private final ArchitectureRepository architectureRepository;

    @Autowired
    private final CharactersRepository charactersRepository;

    @Autowired
    private final ReservationRepository reservationRepository;

    @Autowired
    private final UsersDreamRepository usersDreamRepository;

    private static final Logger logger = LoggerFactory.getLogger(ArchitectService.class);

    private final AuthService authService;

    public List<Architect> getArchitects() {
        return architectureRepository.findAll();
    }

    public List<ArchitectDto> getAllArchitects() {
        return architectureRepository.findAllWithUserDetails().stream().map(architect -> new ArchitectDto(architect.getId(), architect.getUser().getUsername(), architect.getPrice(), architect.getRating())).collect(Collectors.toList());
    }

    public List<Architect> getArchitectRatings() {
        List<Architect> architects = architectureRepository.findAll();
        for (Architect architect : architects) {
            try {
                Double averageRating = architectureRepository.getAverageRatingForArchitect(architect);

                if (averageRating != null) {
                    architect.setRating(averageRating.intValue());
                } else {
                    architect.setRating(1);
                }

            } catch (Exception e) {
                logger.error("Ошибка при вычислении рейтинга для архитектора {}: {}", architect.getId(), e.getMessage());
            }
        }
        return architects;
    }

    public Dream updateDreamPrice(Long dreamId, int price) {
        Dream dream = dreamRepository.findById(dreamId).orElseThrow(() -> new RuntimeException("Dream not found"));
        dream.setPrice(price);
        return dreamRepository.save(dream);
    }

    @Transactional
    public void createTemplate(DreamDto dreamDto, HttpServletRequest request) {
        String username = JWTUtil.extractUsernameFromRequest(request);

        if (username == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized access attempt");
        }

        DreamUser architectUser = authService.getUserByUsername(username);

        if (architectUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found");
        }

        Architect architect = architectureRepository.findByUserId(architectUser.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Architect entry not found"));

        Dream dream = new Dream();
        dream.setName(dreamDto.getName());
        dream.setTimeEra(DreamTimeEra.valueOf(dreamDto.getTimeEra()));
        dream.setVirtualEnvironment(DreamVirtualEnvironment.valueOf(dreamDto.getVirtualEnvironment()));
        dream.setSpecialPowers(DreamSpecialPowers.valueOf(dreamDto.getSpecialPowers()));
        dream.setPhysicalRules(DreamPhysicalRules.valueOf(dreamDto.getPhysicalRules()));
        dream.setRole(DreamRole.valueOf(dreamDto.getRole()));
        dream.setGenre(DreamGenre.valueOf(dreamDto.getGenre()));
        dream.setScenario(dreamDto.getScenario());
        dream.setTemplate(true);
        dream.setPrice(dreamDto.getPrice());
        dream.setArchitect(architect);

        UsersDream usersDream = new UsersDream();
        usersDream.setDream(dream);
        usersDreamRepository.save(usersDream);

        List<Characters> charactersList = dreamDto.getCharacters().stream().map(characterDto -> {
            Characters character = new Characters();
            character.setName(characterDto.getName());
            character.setCharacteristics(characterDto.getCharacteristics());
            character.setAppearance(characterDto.getAppearance());
            character.setRelation(CharactersRelation.valueOf(characterDto.getRelation()));
            character.setOccupation(CharactersOccupation.valueOf(characterDto.getOccupation()));

            charactersRepository.save(character);
            return character;
        }).collect(Collectors.toList());

        dream.setCharacters(new HashSet<>(charactersList));
        dreamRepository.save(dream);
    }

}
