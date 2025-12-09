package com.coursework.repository;

import com.coursework.model.Architect;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface ArchitectRepository extends JpaRepository<Architect, Long>, JpaSpecificationExecutor<Architect> {

    Optional<Architect> findByUserId(Long userId);

    Optional<Architect> findByUserIdAndPrice(Long userId, int price);
}
