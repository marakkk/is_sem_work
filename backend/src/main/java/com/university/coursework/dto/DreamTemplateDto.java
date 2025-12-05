package com.university.coursework.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Data
public class DreamTemplateDto {
    private Long id;
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
    private String architectName;
    private Integer architectRating;
    private Integer architectPrice;
}