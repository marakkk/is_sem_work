package org.marakobz.dto;

import org.marakobz.model.Architect;
import org.springframework.stereotype.Component;

@Component
public class ArchitectMapper {
    public ArchitectDto toDto(Architect architect) {
        if (architect == null) {
            throw new IllegalArgumentException("Architect is null");
        }

        return new ArchitectDto(
                architect.getId(),
                architect.getUser() != null ? architect.getUser().getUsername() : null,
                architect.getPrice(),
                architect.getRating()
        );
    }
}
