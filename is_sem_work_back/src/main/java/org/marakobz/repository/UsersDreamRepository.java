package org.marakobz.repository;

import org.marakobz.model.Dream;
import org.marakobz.model.UsersDream;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersDreamRepository extends JpaRepository<UsersDream, Long> {
    Optional<UsersDream> findByDream(Dream dream);

}