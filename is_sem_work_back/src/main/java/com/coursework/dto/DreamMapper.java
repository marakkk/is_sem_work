package com.coursework.dto;

import com.coursework.model.Dream;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class DreamMapper {
    public DreamDto toDto(Dream dream) {
        if (dream == null) {
            throw new IllegalArgumentException("Dream is null");
        }

        DreamDto dto = new DreamDto();
        dto.setName(dream.getName());
        dto.setTimeEra(dream.getTimeEra().toString());
        dto.setVirtualEnvironment(dream.getVirtualEnvironment().toString());
        dto.setSpecialPowers(dream.getSpecialPowers().toString());
        dto.setPhysicalRules(dream.getPhysicalRules().toString());
        dto.setRole(dream.getRole().toString());
        dto.setScenario(dream.getScenario());
        dto.setTemplate(dream.isTemplate());
        dto.setGenre(dream.getGenre().toString());
        dto.setPrice(dream.getPrice());
        dto.setArchitectId(dream.getArchitect() != null ? dream.getArchitect().getId() : null);
        dto.setCharacters(
                dream.getCharacters().stream()
                        .map(character -> new CharactersDto()) // Дополните маппинг
                        .collect(Collectors.toList())
        );

        return dto;
    }
}

