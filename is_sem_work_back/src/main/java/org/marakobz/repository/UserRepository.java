package org.marakobz.repository;

import org.marakobz.enums.Roles;
import org.marakobz.model.DreamUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<DreamUser, Long> {

    DreamUser findByUsername(String username);
    List<DreamUser> findByRole(Roles role);
}
