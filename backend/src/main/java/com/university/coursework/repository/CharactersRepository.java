package com.university.coursework.repository;

import com.university.coursework.model.Characters;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CharactersRepository extends JpaRepository<Characters, Long> {
    Optional<Characters> findById(Long id);
}
