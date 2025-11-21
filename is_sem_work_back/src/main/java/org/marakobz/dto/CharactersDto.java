package org.marakobz.dto;

import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
public class CharactersDto {

    private String name;
    private String characteristics;
    private String appearance;
    private String relation;
    private String occupation;
    private List<Long> dreamIds;  //problem potential


}
