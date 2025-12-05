package com.coursework.repository;

import com.coursework.enums.Roles;
import com.coursework.model.DreamUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<DreamUser, Long> {
    DreamUser findByUsername(String username);

    List<DreamUser> findByRole(Roles role);
}
