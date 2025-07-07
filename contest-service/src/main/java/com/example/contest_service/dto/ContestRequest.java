package com.example.contest_service.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ContestRequest {
    @NotBlank
    private String title;

    private String description;

    @NotNull
    private LocalDateTime startTime;

    @NotNull
    private LocalDateTime endTime;

    @NotNull
    private List<UUID> questionIds;

    private String createdBy;
}
