package org.marakobz.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.marakobz.dto.ArchitectDto;
import org.marakobz.dto.DreamDto;
import org.marakobz.enums.*;
import org.marakobz.model.*;
import org.marakobz.repository.*;
import org.marakobz.security.JWTUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;


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

    public ArchitectService(DreamRepository dreamRepository, ArchitectureRepository architectRepository, CharactersRepository charactersRepository, AuthService authService, ReservationRepository reservationRepository, UsersDreamRepository usersDreamRepository) {
        this.dreamRepository = dreamRepository;
        this.architectureRepository = architectRepository;
        this.charactersRepository = charactersRepository;
        this.authService = authService;
        this.reservationRepository = reservationRepository;
        this.usersDreamRepository = usersDreamRepository;
    }

    public List<Architect> getArchitects() {
        return architectureRepository.findAll();
    }

    public List<ArchitectDto> getAllArchitects() {
        return architectureRepository.findAllWithUserDetails().stream()
                .map(architect -> new ArchitectDto(
                        architect.getId(),
                        architect.getUser().getUsername(),
                        architect.getPrice(),
                        architect.getRating()
                ))
                .collect(Collectors.toList());
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
    public Dream createTemplate(DreamDto dreamDto, HttpServletRequest request) {

        try {

            String username = JWTUtil.extractUsernameFromRequest(request);
            if (username == null) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized access attempt");
            }
            DreamUser architectUser = authService.getUserByUsername(username);
            if (architectUser == null) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found");
            }

            Architect architect = architectureRepository.findByUserId(architectUser.getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Architect entry not found"));


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

            List<Characters> charactersList = dreamDto.getCharacters().stream()
                    .map(characterDto -> {
                        Characters character = new Characters();
                        character.setName(characterDto.getName());
                        character.setCharacteristics(characterDto.getCharacteristics());
                        character.setAppearance(characterDto.getAppearance());
                        character.setRelation(CharactersRelation.valueOf(characterDto.getRelation()));
                        character.setOccupation(CharactersOccupation.valueOf(characterDto.getOccupation()));

                        charactersRepository.save(character);
                        return character;
                    })
                    .collect(Collectors.toList());

            dream.setCharacters(new HashSet<>(charactersList));
            Dream savedDream = dreamRepository.save(dream);


            return savedDream;
        } catch (Exception e) {
            logger.error("Ошибка при создании шаблона сна: ", e);
            throw e;
        }
    }

}
