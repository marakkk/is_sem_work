package com.university.coursework.dto;

import lombok.Data;
import java.util.List;

@Data
public class CharactersDto {
    private String name;
    private String characteristics;
    private String appearance;
    private String relation;
    private String occupation;
    private List<Long> dreamIds;
}
