package com.university.coursework.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class ReservationTemplateDto {
    private Long originalTemplateId;
    private Long architectId;
    private String date;
    private String time;
    private String timeOfReservation;
}
