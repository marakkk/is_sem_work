package com.coursework.repository;

import com.coursework.enums.AdminStatus;
import com.coursework.model.Architect;
import com.coursework.model.DreamUser;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ArchitectureRepository extends JpaRepository<Architect, Long> {

    List<Architect> findAll();

    Optional<Architect> findByUserId(Long usersId);

    Optional<Architect> findByUser(DreamUser user); // Add this method

    @Query("SELECT a FROM Architect a JOIN a.user u")
    List<Architect> findAllWithUserDetails();

    @Query("SELECT AVG(r.mark) FROM Review r WHERE r.architect = :architect")
    Double getAverageRatingForArchitect(Architect architect);

    @EntityGraph(attributePaths = {"user"})
    List<Architect> findByStatus(AdminStatus status);
}
