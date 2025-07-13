package com.example.contest_service.repository;

import com.example.contest_service.model.ContestAttempt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContestAttemptRepository extends JpaRepository<ContestAttempt, Long> {
    Optional<ContestAttempt> findByUserIdAndContestId(Long userId, Long contestId);
}
