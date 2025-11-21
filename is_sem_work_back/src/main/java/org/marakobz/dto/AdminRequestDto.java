package org.marakobz.dto;

import lombok.Data;

@Data
public class AdminRequestDto {
    private Long id;
    private String username; // DreamUser's username
    private Long userId; // DreamUser's ID
    private String status;
}