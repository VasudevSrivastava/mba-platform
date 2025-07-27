package com.example.contest_service.repository;

import com.example.contest_service.model.AnswerSubmission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerSubmissionRepository extends JpaRepository<AnswerSubmission, Long> {
    //public findByContest
}
