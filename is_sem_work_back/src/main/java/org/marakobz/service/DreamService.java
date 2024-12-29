package org.marakobz.service;

import org.marakobz.dto.DreamDto;
import org.marakobz.enums.*;
import org.marakobz.model.*;
import org.marakobz.repository.CharactersRepository;
import org.marakobz.repository.DreamRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DreamService {

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
            dream.setPrice(dreamDto.getPrice());

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


}

