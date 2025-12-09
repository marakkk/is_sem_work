package com.coursework.repository;

import com.coursework.enums.CalendarStatus;
import com.coursework.model.Calendar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface CalendarRepository extends JpaRepository<Calendar, Long> {

    List<Calendar> findByDateAndTime(LocalDate date, LocalTime time);

    List<Calendar> findByDateAndTimeAndStatus(LocalDate date, LocalTime time, CalendarStatus status);
}
