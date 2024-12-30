package org.marakobz.service;

import org.marakobz.dto.DreamDto;
import org.marakobz.dto.DreamUserDto;
import org.marakobz.enums.*;
import org.marakobz.model.*;
import org.marakobz.repository.ArchitectureRepository;
import org.marakobz.repository.CharactersRepository;
import org.marakobz.repository.DreamRepository;
import org.marakobz.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
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

    public Dream createOwnDream(DreamDto dreamDto) {
        try {
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
    public List<DreamDto> getTemplateDreams() {
        List<Dream> dreams = dreamRepository.findByTemplateTrue();
        List<DreamDto> dreamDtos = new ArrayList<>();

        for (Dream dream : dreams) {
            DreamDto dreamDto = new DreamDto();
            dreamDto.setName(dream.getName());
            dreamDto.setTimeEra(dream.getTimeEra().toString());
            dreamDto.setVirtualEnvironment(dream.getVirtualEnvironment().toString());
            dreamDto.setSpecialPowers(dream.getSpecialPowers().toString());
            dreamDto.setPhysicalRules(dream.getPhysicalRules().toString());
            dreamDto.setRole(dream.getRole().toString());
            dreamDto.setGenre(dream.getGenre().toString());
            dreamDto.setScenario(dream.getScenario());

            dreamDtos.add(dreamDto);
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


    public Dream assignArchitectToDream(Long dreamId, Long architectId) {
        Dream dream = dreamRepository.findById(dreamId)
                .orElseThrow(() -> new RuntimeException("Dream not found"));

        DreamUser architect = userRepository.findById(architectId)
                .orElseThrow(() -> new RuntimeException("Architect not found"));

        Architect architectEntity = new Architect();
        architectEntity.setUser(architect);
        architectEntity.setDreamId(dreamId);

        dream.getArchitects().add(architectEntity);

        dreamRepository.save(dream);
        return dream;
    }



}


