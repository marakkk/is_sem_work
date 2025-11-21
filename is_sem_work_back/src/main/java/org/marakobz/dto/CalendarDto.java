package org.marakobz.dto;

import lombok.Data;
import org.marakobz.enums.CalendarStatus;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class CalendarDto {

    private Long calendarId;
    private String time;
    private String date;
    private String status;

    public CalendarDto(Long id, LocalDate date, LocalTime time, CalendarStatus status) {
        this.calendarId = id;
        this.date = String.valueOf(date);
        this.time = String.valueOf(time);
        this.status = String.valueOf(status);
    }
}
