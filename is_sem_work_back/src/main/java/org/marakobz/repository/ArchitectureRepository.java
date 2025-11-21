package org.marakobz.repository;

import org.marakobz.enums.AdminStatus;
import org.marakobz.model.Architect;
import org.marakobz.model.Dream;
import org.marakobz.model.DreamUser;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArchitectureRepository extends JpaRepository<Architect, Long> {
    List<Architect> findAllByOrderByRatingDesc();
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
