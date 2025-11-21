package org.marakobz.repository;

import org.marakobz.enums.CalendarStatus;
import org.marakobz.model.Dream;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.query.Param;

public interface DreamRepository extends JpaRepository<Dream, Long> {

    Optional<Dream> findById(Long id);

    List<Dream> findByTemplateTrue();

    @Query("SELECT d FROM Dream d " +
            "JOIN FETCH d.architect a " +
            "WHERE d.template = true")
    List<Dream> findTemplateDreamsWithArchitect();

}
