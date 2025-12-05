package com.coursework.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@Data
public class ReservationDto {
    private Long reservationId;
    private String dreamName;
    private String timeEra;
    private String virtualEnvironment;
    private String specialPowers;
    private String physicalRules;
    private String role;
    private String genre;
    private String scenario;

    @Getter
    @Setter
    private boolean template;
    private double price;
    private List<CharactersDto> characters;
    private Long architectId;
    private Long usersDreamsId;
    private double architectPrice;
    private String date;
    private String time;
    private String status;
    private String timeOfReservation;

}
