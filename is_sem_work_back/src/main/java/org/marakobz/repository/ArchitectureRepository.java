package org.marakobz.repository;

import org.marakobz.model.Architect;
import org.marakobz.model.Dream;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArchitectureRepository extends JpaRepository<Architect, Long> {
    List<Architect> findAllByOrderByRatingDesc();

}
