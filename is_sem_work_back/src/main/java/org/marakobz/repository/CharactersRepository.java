package org.marakobz.repository;

import org.marakobz.model.Characters;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CharactersRepository extends JpaRepository<Characters, Long> {

    Optional<Characters> findById(Long id);
}
