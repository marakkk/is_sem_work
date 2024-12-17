package org.marakobz.repository;

import org.marakobz.model.DreamUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<DreamUser, Long> {

    DreamUser findByUsername(String username);

}
