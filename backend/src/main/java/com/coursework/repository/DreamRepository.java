package com.coursework.repository;

import com.coursework.model.Dream;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface DreamRepository extends JpaRepository<Dream, Long> {

    Optional<Dream> findById(Long id);

    @EntityGraph(attributePaths = {"architect"})
    List<Dream> findByTemplateTrue();
}