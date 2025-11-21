package org.marakobz.repository;

import org.marakobz.enums.CalendarStatus;
import org.marakobz.model.Calendar;
import org.marakobz.model.Dream;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface CalendarRepository extends JpaRepository<Calendar, Long> {

    @Query("SELECT c FROM Calendar c WHERE c.date = :date AND c.time = :time AND c.status = :status")
    List<Calendar> findByDateAndTimeAndStatus(@Param("date") LocalDate date,
                                              @Param("time") LocalTime time,
                                              @Param("status") CalendarStatus status);

    @Query("SELECT c FROM Calendar c WHERE c.date = :date AND c.time = :time")
    List<Calendar> findByDateAndTime(@Param("date") LocalDate date,
                                     @Param("time") LocalTime time);
}

