package org.marakobz.dto;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
public class DreamDto {
    private Long dreamId;
    private String name;
    private Integer price;
    private Long architectId;
    private List<Long> characterIds;
}
