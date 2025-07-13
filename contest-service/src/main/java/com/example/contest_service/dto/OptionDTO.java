package com.example.contest_service.dto;


import lombok.Data;

import java.util.UUID;

@Data
public class OptionDTO {
    private String text;
    private boolean isCorrect;
}
