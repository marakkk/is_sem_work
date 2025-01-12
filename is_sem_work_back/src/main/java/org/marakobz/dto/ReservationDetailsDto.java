package org.marakobz.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.ZonedDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class ReservationDetailsDto {
    private String dreamName;
    private String timeEra;
    private String virtualEnvironment;
    private String specialPowers;
    private String physicalRules;
    private String role;
    private String genre;
    private String scenario;
    private boolean template;
    private double price;
    private String date;
    private String time;
    private String timeOfReservation;
    private String architectUsername; // New field
    private String status;

}
