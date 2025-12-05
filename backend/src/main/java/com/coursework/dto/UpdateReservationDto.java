package com.coursework.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class UpdateReservationDto {
    private String date;          // New date for the reservation
    private String time;          // New time for the reservation
    private Long architectId;     // New architect ID
    private int price;         // New price
}