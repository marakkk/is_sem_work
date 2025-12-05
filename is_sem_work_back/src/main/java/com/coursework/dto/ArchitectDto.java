package com.coursework.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ArchitectDto {
    private Long architectId;
    private String username;
    private Integer price;
    private Integer rating;

}
