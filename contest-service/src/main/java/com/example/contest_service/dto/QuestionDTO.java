package com.example.contest_service.dto;

import lombok.Data;
import java.util.List;
import java.util.UUID;

@Data
public class QuestionDTO {
    private UUID id;
    private String text;
    private String image_url;
    private List<OptionDTO> options;
}
