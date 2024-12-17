package org.marakobz.dto;

import lombok.Data;

@Data
public class ImportHistoryDto {
    private Long id;
    private String status;
    private int addedObjects;
    private String startTime;
    private String creatorName;
    private String fileUrl;
    private String fileName;
}