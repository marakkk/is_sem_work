package com.coursework.repository;

import com.coursework.model.Reservation;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    Optional<Reservation> findById(Long id);

    @EntityGraph(attributePaths = {"dream", "architect", "calendar"})
    List<Reservation> findByUserId(Long userId);

    @EntityGraph(attributePaths = {"dream", "architect", "calendar"})
    List<Reservation> findByArchitectId(Long architectId);
}
