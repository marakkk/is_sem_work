package com.university.coursework.service;

import com.university.coursework.dto.DreamDto;
import com.university.coursework.dto.DreamTemplateDto;
import com.university.coursework.dto.DreamTemplateMapper;
import com.university.coursework.dto.DreamUserDto;
import com.university.coursework.enums.*;
import com.university.coursework.model.Characters;
import com.university.coursework.model.Dream;
import com.university.coursework.model.DreamUser;
import com.university.coursework.model.UsersDream;
import com.university.coursework.repository.CharactersRepository;
import com.university.coursework.repository.DreamRepository;
import com.university.coursework.repository.UserRepository;
import com.university.coursework.repository.UsersDreamRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import com.university.coursework.security.JWTUtil;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class DreamService {
    @Autowired
    private UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(DreamService.class);

    @Autowired
    private DreamRepository dreamRepository;

    @Autowired
    private CharactersRepository charactersRepository;

    @Autowired
    private UsersDreamRepository usersDreamRepository;

    private final AuthService authService;

    @Transactional
    public void createOwnDream(DreamDto dreamDto, HttpServletRequest request) {
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

        UsersDream usersDream = new UsersDream();
        usersDream.setDream(dream);
        usersDream.setUser(creator);
        usersDreamRepository.save(usersDream);

        logger.info(usersDream.toString());

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
        dreamRepository.save(dream);
    }

    @Transactional
    public List<DreamTemplateDto> getTemplateDreams() {
        List<Dream> dreams = dreamRepository.findTemplateDreamsWithArchitect();

        DreamTemplateMapper mapper = new DreamTemplateMapper();

        List<DreamTemplateDto> dreamDtos = dreams.stream()
                .map(mapper::dreamToDreamTemplateDto)
                .collect(Collectors.toList());

        logger.info("Mapped DreamTemplate DTOs:");
        for (DreamTemplateDto dto : dreamDtos) {
            logger.info(String.valueOf(dto));
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


