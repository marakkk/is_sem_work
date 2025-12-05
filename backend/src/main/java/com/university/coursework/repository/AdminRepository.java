package com.university.coursework.repository;

import com.university.coursework.enums.AdminStatus;
import com.university.coursework.model.Admin;
import com.university.coursework.model.DreamUser;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByUser(DreamUser user);

    @EntityGraph(attributePaths = {"user"})
    List<Admin> findByStatus(AdminStatus status);
}