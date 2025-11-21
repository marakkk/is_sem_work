package org.marakobz.dto;

import org.marakobz.model.Calendar;
import org.springframework.stereotype.Component;

@Component
public class CalendarMapper {
    public String toFormattedDate(Calendar calendar) {
        if (calendar == null) {
            throw new IllegalArgumentException("Calendar is null");
        }
        return calendar.getDate().format(java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }

    public String toFormattedTime(Calendar calendar) {
        if (calendar == null) {
            throw new IllegalArgumentException("Calendar is null");
        }
        return calendar.getTime().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

    public String toStatus(Calendar calendar) {
        if (calendar == null) {
            throw new IllegalArgumentException("Calendar is null");
        }
        return calendar.getStatus().toString();
    }

}

