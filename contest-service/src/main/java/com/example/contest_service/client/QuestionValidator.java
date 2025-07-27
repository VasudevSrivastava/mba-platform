package com.example.contest_service.client;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClientException;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
@RequiredArgsConstructor
public class QuestionValidator {

    private static final Logger logger = LoggerFactory.getLogger(QuestionValidator.class);
    private final RestTemplate restTemplate;

    public boolean areValidQuestionIds(List<UUID> questionIds, String authHeader) {
        String url = "http://127.0.0.1:8000/api/questions/batch/";

        List<String> uuidStrings = questionIds.stream()
                .map(UUID::toString)
                .toList();

        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("id_list", uuidStrings);  // ← now list of Strings


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", authHeader);
        logger.info("authHeader:  {} ", authHeader);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestMap, headers);
        logger.info("QuestionValidator | Sending IDs: {}", questionIds);
        logger.info("QuestionValidator | Auth Header: {}", authHeader);
        logger.info("QuestionValidator | Payload Map: {}", requestMap);

        try {
            ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    requestEntity,
                    new ParameterizedTypeReference<>() {}
            );
            logger.info("HTTP Status: {}", response.getStatusCode());
            logger.info("Response Body: {}", response.getBody());

            List<Map<String, Object>> questions = response.getBody();
            return questions != null && questions.size() == questionIds.size();

        } catch (HttpStatusCodeException ex) {
            logger.error("HTTP Error: {}", ex.getStatusCode());
            logger.error("Response Body: {}", ex.getResponseBodyAsString());
            return false;
        } catch (Exception ex) {
            logger.error("Other Error: {}", ex.getMessage(), ex);
            return false;
        }



    }
}
