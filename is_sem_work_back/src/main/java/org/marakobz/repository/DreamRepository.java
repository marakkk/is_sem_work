package org.marakobz.repository;

import org.marakobz.model.Dream;
import org.marakobz.model.DreamUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.List;
import java.util.Optional;

@Repository
public interface DreamRepository extends JpaRepository<Dream, Long>,  JpaSpecificationExecutor<Dream>{
    Optional<Dream> findById(Long id);

}
