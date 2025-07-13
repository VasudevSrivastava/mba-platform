package com.example.contest_service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
public class AnswerSubmission {
    @Id @GeneratedValue
    private Long id;
    private Long userId;
    private Long contestId;
    private UUID questionId;
    private boolean isCorrect;
    private LocalDateTime submittedAt;
}
