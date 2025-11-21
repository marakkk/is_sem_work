package org.marakobz.dto;

import org.marakobz.model.Dream;

public class DreamTemplateMapper {

    public DreamTemplateDto dreamToDreamTemplateDto(Dream dream) {
        if (dream == null) {
            return null;
        }

        DreamTemplateDto dto = new DreamTemplateDto();
        dto.setId(dream.getId());
        dto.setName(dream.getName());
        dto.setTimeEra(dream.getTimeEra().toString());
        dto.setVirtualEnvironment(dream.getVirtualEnvironment().toString());
        dto.setSpecialPowers(dream.getSpecialPowers().toString());
        dto.setPhysicalRules(dream.getPhysicalRules().toString());
        dto.setRole(dream.getRole().toString());
        dto.setScenario(dream.getScenario().toString());
        dto.setGenre(dream.getGenre().toString());
        dto.setPrice(dream.getPrice());
        dto.setArchitectId(dream.getArchitect().getId());
        dto.setTemplate(true);
        dto.setArchitectName(dream.getArchitect().getUser().getUsername());
        dto.setArchitectRating(dream.getArchitect().getRating());
        dto.setArchitectPrice(dream.getArchitect().getPrice());


        return dto;
    }
}
