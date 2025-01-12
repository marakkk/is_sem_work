package org.marakobz.repository;

import org.marakobz.model.Dream;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface DreamRepository extends JpaRepository<Dream, Long> {

    Optional<Dream> findById(Long id);
    List<Dream> findByTemplateTrue();

    @Query("SELECT d FROM Dream d " +
            "JOIN FETCH d.architect a " +  // Fetching architect to avoid LazyInitializationException
            "WHERE d.template = true")
    List<Dream> findTemplateDreamsWithArchitect();

}

