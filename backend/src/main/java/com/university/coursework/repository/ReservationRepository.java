package com.university.coursework.repository;

import com.university.coursework.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    Optional<Reservation> findById(Long id);

    @Query("SELECT r FROM Reservation r " +
            "JOIN FETCH r.dream d " +
            "JOIN FETCH r.architect a " +
            "JOIN FETCH r.calendar c " +
            "WHERE r.user.id = :userId")
    List<Reservation> findReservationsByUserId(@Param("userId") Long userId);

    @Query("SELECT r FROM Reservation r " +
            "JOIN FETCH r.dream d " +
            "JOIN FETCH r.architect a " +
            "JOIN FETCH r.calendar c " +
            "WHERE r.architect.id = :architectId")
    List<Reservation> findReservationsByArchitectId(@Param("architectId") Long architectId);
}

