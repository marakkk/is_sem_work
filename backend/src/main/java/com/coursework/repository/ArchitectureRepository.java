package com.coursework.repository;

import com.coursework.enums.AdminStatus;
import com.coursework.model.Architect;
import com.coursework.model.DreamUser;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArchitectureRepository extends JpaRepository<Architect, Long> {

    Optional<Architect> findByUser(DreamUser user);

    Optional<Architect> findByUserId(Long userId);

    @EntityGraph(attributePaths = {"user"})
    List<Architect> findAll();

    @EntityGraph(attributePaths = {"user"})
    List<Architect> findByStatus(AdminStatus status);
}
