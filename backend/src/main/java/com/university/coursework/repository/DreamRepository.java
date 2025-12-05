package com.university.coursework.repository;

import com.university.coursework.model.Dream;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface DreamRepository extends JpaRepository<Dream, Long> {

    Optional<Dream> findById(Long id);

    @Query("SELECT d FROM Dream d " +
            "JOIN FETCH d.architect a " +
            "WHERE d.template = true")
    List<Dream> findTemplateDreamsWithArchitect();

}
