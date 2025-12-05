package com.university.coursework.dto;

import com.university.coursework.model.Architect;
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
