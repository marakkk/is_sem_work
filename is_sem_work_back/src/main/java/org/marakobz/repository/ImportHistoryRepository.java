package org.marakobz.repository;

import org.marakobz.model.ImportHistory;
import org.marakobz.model.DreamUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImportHistoryRepository extends JpaRepository<ImportHistory, Long> {
    List<ImportHistory> findByUser(DreamUser user);

}
