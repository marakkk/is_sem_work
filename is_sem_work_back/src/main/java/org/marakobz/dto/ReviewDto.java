package org.marakobz.dto;

import lombok.Data;

@Data
public class ReviewDto {
    private Long reviewId;
    private Integer mark;
    private Long usersDreamsId;
    private Long architectId;
}
