package com.example.contest_service.dto;

import java.util.List;
import java.util.UUID;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class ContestResponse {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private List<UUID> questionIds;
    private String createdBy;
}
