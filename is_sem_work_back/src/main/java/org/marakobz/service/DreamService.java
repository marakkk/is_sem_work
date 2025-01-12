package org.marakobz.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.marakobz.dto.DreamDto;
import org.marakobz.dto.DreamTemplateDto;
import org.marakobz.dto.DreamTemplateMapper;
import org.marakobz.dto.DreamUserDto;
import org.marakobz.enums.*;
import org.marakobz.model.*;
import org.marakobz.repository.ArchitectureRepository;
import org.marakobz.repository.CharactersRepository;
import org.marakobz.repository.DreamRepository;
import org.marakobz.repository.UserRepository;
import org.marakobz.security.JWTUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DreamService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ArchitectureRepository architectureRepository;

    private static final Logger logger = LoggerFactory.getLogger(DreamService.class);

    @Autowired
    private DreamRepository dreamRepository;

    @Autowired
    private CharactersRepository charactersRepository;

    private final AuthService authService;

    public DreamService(AuthService authService) {
        this.authService = authService;
    }

    @Transactional
    public Dream createOwnDream(DreamDto dreamDto, HttpServletRequest request) {

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
            dream.setName(dreamDto.getName());
            dream.setTimeEra(DreamTimeEra.valueOf(dreamDto.getTimeEra()));
            dream.setVirtualEnvironment(DreamVirtualEnvironment.valueOf(dreamDto.getVirtualEnvironment()));
            dream.setSpecialPowers(DreamSpecialPowers.valueOf(dreamDto.getSpecialPowers()));
            dream.setPhysicalRules(DreamPhysicalRules.valueOf(dreamDto.getPhysicalRules()));
            dream.setRole(DreamRole.valueOf(dreamDto.getRole()));
            dream.setGenre(DreamGenre.valueOf(dreamDto.getGenre()));
            dream.setScenario(dreamDto.getScenario());
            dream.setTemplate(false);
            dream.setPrice(0);
            dream.setCreator(creator);

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
            return dreamRepository.save(dream);
        } catch (Exception e) {
            logger.error("Ошибка при создании сна: ", e);
            throw e;
        }
    }


    @Transactional
    public List<DreamTemplateDto> getTemplateDreams() {
        List<Dream> dreams = dreamRepository.findTemplateDreamsWithArchitect(); // Используем кастомный запрос
        if (dreams.isEmpty()) {
            return Collections.emptyList();
        }

        DreamTemplateMapper mapper = new DreamTemplateMapper();

        List<DreamTemplateDto> dreamDtos = dreams.stream()
                .map(mapper::dreamToDreamTemplateDto)
                .collect(Collectors.toList());

        System.out.println("Mapped DreamTemplate DTOs:");
        for (DreamTemplateDto dto : dreamDtos) {
            System.out.println(dto);
        }

        return dreamDtos;
    }



    public List<DreamUserDto> getArchitects() {
        List<DreamUser> architects = userRepository.findByRole(Roles.ARCHITECT);

        return architects.stream()
                .map(architect -> {
                    DreamUserDto dto = new DreamUserDto();
                    dto.setUsersId(architect.getId());
                    dto.setUsername(architect.getUsername());
                    dto.setRole(architect.getRole().toString());

                    return dto;
                })
                .collect(Collectors.toList());
    }

    public List<Characters> getCharactersByDreamId(Long dreamId) {
        logger.info("Received ID: {}", dreamId);

        Dream dream = dreamRepository.findById(dreamId)
                .orElseThrow(() -> new RuntimeException("Dream not found with ID: " + dreamId));
        return new ArrayList<>(dream.getCharacters());
    }


}


