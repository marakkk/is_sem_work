package org.marakobz.service;

import org.marakobz.dto.CalendarDto;
import org.marakobz.model.Calendar;
import org.marakobz.repository.CalendarRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CalendarService {

    private final CalendarRepository calendarRepository;

    public CalendarService(CalendarRepository calendarRepository) {
        this.calendarRepository = calendarRepository;
    }

    public List<CalendarDto> getCalendarEntries() {
        List<Calendar> calendarList = calendarRepository.findAll();
        return calendarList.stream().map(calendar -> {
            CalendarDto dto = new CalendarDto();
            dto.setCalendarId(calendar.getId());
            dto.setTime(calendar.getTime().toString());
            dto.setDate(calendar.getDate().toString());
            dto.setStatus(calendar.getStatus().name());
            return dto;
        }).collect(Collectors.toList());
    }
}
