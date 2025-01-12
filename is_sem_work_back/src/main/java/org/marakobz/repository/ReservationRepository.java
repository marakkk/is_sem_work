package org.marakobz.repository;

import org.marakobz.model.DreamUser;
import org.marakobz.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {


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

