package com.coursework.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class ReservationTemplateDto {
    private Long originalTemplateId; // ID of the original template
    private Long architectId;       // Architect associated with the template
    private String date;            // Reservation date
    private String time;            // Reservation time
    private String timeOfReservation; // Timestamp for when the reservation is made
}
