package com.university.coursework.dto;

import lombok.Data;

@Data
public class ArchitectRequestDto {
    private Long id;
    private String username;
    private Long userId;
    private String status;
}