package com.example.contest_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class AnswerSubmissionRequest {

    @NotNull
    UUID questionId;

    @NotNull
    Long optionId;

    @NotBlank
    String optionText;
}
