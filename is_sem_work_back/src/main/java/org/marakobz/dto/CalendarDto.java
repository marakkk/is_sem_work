package org.marakobz.dto;

import lombok.Data;

@Data
public class CalendarDto {
    private Long calendarId;
    private String time;
    private String date;
    private String status;
}
