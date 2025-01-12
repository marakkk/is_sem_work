package org.marakobz.repository;

import org.marakobz.model.Architect;
import org.marakobz.model.Dream;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ArchitectureRepository extends JpaRepository<Architect, Long> {
    List<Architect> findAllByOrderByRatingDesc();
    List<Architect> findAll();
    Optional<Architect> findByUserId(Long usersId);
    @Query("SELECT a FROM Architect a JOIN a.user u")
    List<Architect> findAllWithUserDetails();

}
