package com.example.contest_service.client;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClientException;

import java.util.*;

@Component
@RequiredArgsConstructor
public class QuestionValidator {

    private final RestTemplate restTemplate;

    public boolean areValidQuestionIds(List<UUID> questionIds, String authHeader) {
        String url = "http://127.0.0.1:8000/api/questions/batch/";

        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("id_list", questionIds);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", authHeader);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestMap, headers);

        try {
            ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    requestEntity,
                    new ParameterizedTypeReference<>() {}
            );

            List<Map<String, Object>> questions = response.getBody();

            return questions != null && questions.size() == questionIds.size();

        } catch (RestClientException ex) {
            System.err.println("Error calling QuestionService: " + ex.getMessage());
            return false;
        }
    }

}
