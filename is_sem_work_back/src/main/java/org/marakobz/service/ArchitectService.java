package org.marakobz.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.marakobz.dto.ArchitectDto;
import org.marakobz.dto.DreamDto;
import org.marakobz.enums.*;
import org.marakobz.model.Architect;
import org.marakobz.model.Characters;
import org.marakobz.model.Dream;
import org.marakobz.model.DreamUser;
import org.marakobz.repository.ArchitectureRepository;
import org.marakobz.repository.CharactersRepository;
import org.marakobz.repository.DreamRepository;
import org.marakobz.security.JWTUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class ArchitectService {

    private final DreamRepository dreamRepository;
    private final ArchitectureRepository architectureRepository;
    private final CharactersRepository charactersRepository;
    private static final Logger logger = LoggerFactory.getLogger(ArchitectService.class);
    private final AuthService authService;

    public ArchitectService(DreamRepository dreamRepository, ArchitectureRepository architectRepository, CharactersRepository charactersRepository, AuthService authService) {
        this.dreamRepository = dreamRepository;
        this.architectureRepository = architectRepository;
        this.charactersRepository = charactersRepository;
        this.authService = authService;
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


    public List<Dream> getRequestsForDreams() {
        return dreamRepository.findByTemplateTrue();
    }

    public List<Architect> getArchitectRatings() {
        return architectureRepository.findAllByOrderByRatingDesc();
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
