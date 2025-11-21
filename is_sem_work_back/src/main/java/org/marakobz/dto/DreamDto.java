package org.marakobz.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Data
public class DreamDto {
    private String name;
    private String timeEra;
    private String virtualEnvironment;
    private String specialPowers;
    private String physicalRules;
    private String role;
    private String scenario;


    @Getter
    @Setter
    private boolean template;

    private String genre;
    private Integer price;
    private Long architectId;
    private List<CharactersDto> characters;


}
