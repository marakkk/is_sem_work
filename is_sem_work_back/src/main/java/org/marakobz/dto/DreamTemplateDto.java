package org.marakobz.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Data
public class DreamTemplateDto {
    private Long id;  // Add the id field here
    private String name;
    private String timeEra;
    private String virtualEnvironment;
    private String specialPowers;
    private String physicalRules;
    private String role;
    private String scenario;
    private String genre;
    private Integer price;
    private Long architectId;
    private boolean template;
    private String architectName;  // New field
    private Integer architectRating;  // New field
    private Integer architectPrice;
}