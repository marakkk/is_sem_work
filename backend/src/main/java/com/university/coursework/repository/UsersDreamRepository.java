package com.university.coursework.repository;

import com.university.coursework.model.Dream;
import com.university.coursework.model.UsersDream;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersDreamRepository extends JpaRepository<UsersDream, Long> {
    Optional<UsersDream> findByDream(Dream dream);
}