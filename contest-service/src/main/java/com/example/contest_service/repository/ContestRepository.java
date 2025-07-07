package com.example.contest_service.repository;

import com.example.contest_service.model.Contest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;
import java.time.LocalDateTime;

public interface ContestRepository extends JpaRepository<Contest,Long> {
    Optional<Contest> findByTitle(String title);
    List<Contest> findByCreatedBy(String createdBy);
    List<Contest> findByStartTimeBeforeAndEndTimeAfter(LocalDateTime now1, LocalDateTime now2);

}
