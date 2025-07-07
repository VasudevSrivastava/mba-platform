package com.example.contest_service.service;

import com.example.contest_service.dto.ContestRequest;
import com.example.contest_service.dto.ContestResponse;
import com.example.contest_service.repository.ContestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.example.contest_service.model.Contest;
import org.springframework.web.client.RestTemplate;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ContestService {
    private final ContestRepository contestRepository;
    private final RestTemplate restTemplate;

    public void create(ContestRequest request){
        if (!validQuestionIds(request.getQuestionIds())){
            throw(new IllegalArgumentException("Some question IDs are invalid"));
        }
        var contest = Contest.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .createdBy(request.getCreatedBy())
                .questionIds(request.getQuestionIds())
                .build();

        contestRepository.save(contest);
    }

    public void update(Long id, ContestRequest request){
        var contest = contestRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Contest not found"));

        contest.setTitle(request.getTitle());
        contest.setDescription(request.getDescription());
        contest.setStartTime(request.getStartTime());
        contest.setEndTime(request.getEndTime());
        contest.setCreatedBy(request.getCreatedBy());
        contest.setQuestionIds(request.getQuestionIds());
        contestRepository.save(contest);
    }

    public void delete(Long id){
        var contest = contestRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Contest not found"));

        contestRepository.deleteById(id);
    }

    public List<ContestResponse> getAllContests(){
        return contestRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    public ContestResponse getContestById(Long id){
        var contest =  contestRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Contest not found with id" + id));
        return mapToResponse(contest);
    }

    private ContestResponse mapToResponse(Contest contest){
        ContestResponse response = new ContestResponse();
        response.setId(contest.getId());
        response.setTitle(contest.getTitle());
        response.setDescription(contest.getDescription());
        response.setQuestionIds(contest.getQuestionIds());
        response.setStartTime(contest.getStartTime());
        response.setEndTime(contest.getEndTime());
        response.setCreatedBy(contest.getCreatedBy());

        return response;
    }

    private boolean validQuestionIds(List<UUID> questionIds){
        String url = "http://127.0.0.1:8000/api/questions/batch/";

        Map<String, Object> request = new HashMap<>();
        request.put("id_list", questionIds);

        questions = restTemplate.body(request);

        if (questions == null || question.size() != questionIds.size()){
            return false;
        }
        return true;
    }
}

