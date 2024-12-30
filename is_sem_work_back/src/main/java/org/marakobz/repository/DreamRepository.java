package org.marakobz.repository;

import org.marakobz.model.Dream;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface DreamRepository extends JpaRepository<Dream, Long> {

    Optional<Dream> findById(Long id);
    List<Dream> findByTemplateTrue();

}

