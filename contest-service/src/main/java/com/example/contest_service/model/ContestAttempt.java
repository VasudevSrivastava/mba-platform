package com.example.contest_service.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Table(
        uniqueConstraints = @UniqueConstraint(columnNames = {"userId", "contestId"})
)
@Entity
public class ContestAttempt {

    @Id
    @GeneratedValue
    private Long id;

    private Long contestId;
    private Long userId;

    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;
    private boolean completed;
}


