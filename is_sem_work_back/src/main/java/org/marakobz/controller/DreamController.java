package org.marakobz.controller;

import org.marakobz.dto.DreamDto;
import org.marakobz.enums.*;
import org.marakobz.model.Characters;
import org.marakobz.model.Dream;
import org.marakobz.model.DreamCharacter;
import org.marakobz.repository.CharactersRepository;
import org.marakobz.repository.DreamRepository;
import org.marakobz.service.DreamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dreams")
public class DreamController {

    @Autowired
    private DreamService dreamService;

    @Autowired
    private DreamRepository dreamRepository;

    @Autowired
    private CharactersRepository charactersRepository;

    @PostMapping("/create-own-dream")
    public Dream createOwnDream(@RequestBody DreamDto dreamDto) {
        Dream dream = new Dream();
        dream.setName(dreamDto.getName());
        dream.setTimeEra(DreamTimeEra.valueOf(dreamDto.getTimeEra()));
        dream.setVirtualEnvironment(DreamVirtualEnvironment.valueOf(dreamDto.getVirtualEnvironment()));
        dream.setSpecialPowers(DreamSpecialPowers.valueOf(dreamDto.getSpecialPowers()));
        dream.setPhysicalRules(DreamPhysicalRules.valueOf(dreamDto.getPhysicalRules()));
        dream.setRole(DreamRole.valueOf(dreamDto.getRole()));
        dream.setGenre(DreamGenre.valueOf(dreamDto.getGenre()));

        List<DreamCharacter> dreamCharacters = dreamDto.getCharacterIds().stream()
                .map(characterId -> {
                    Characters character = charactersRepository.findById(characterId).orElseThrow();
                    DreamCharacter dreamCharacter = new DreamCharacter();
                    dreamCharacter.setCharacter(character);
                    dreamCharacter.setDream(dream);
                    return dreamCharacter;
                })
                .collect(Collectors.toList());

        List<Characters> charactersList = dreamDto.getCharacterIds().stream()
                .map(characterId -> charactersRepository.findById(characterId).orElseThrow())
                .collect(Collectors.toList());

        dream.setCharacters(new HashSet<>(charactersList));

        return dreamRepository.save(dream);
    }

}
