package org.marakobz.dto;

import lombok.Data;

import java.util.List;

@Data
public class CharactersDto {
    private Long charactersId;
    private String name;
    private List<Long> dreamIds;
}
