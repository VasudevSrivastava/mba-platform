package com.example.contest_service.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class AnswerSubmissionRequest {
    UUID questionId;
    Long optionId;
    String optionText;
}
