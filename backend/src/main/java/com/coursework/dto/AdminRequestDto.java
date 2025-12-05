package com.coursework.dto;

import lombok.Data;

@Data
public class AdminRequestDto {
    private Long id;
    private String username;
    private Long userId;
    private String status;
}