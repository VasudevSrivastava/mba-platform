package com.example.contest_service.service;
import com.example.contest_service.client.QuestionValidator;
import com.example.contest_service.dto.ContestRequest;
import com.example.contest_service.dto.ContestResponse;
import com.example.contest_service.dto.QuestionDTO;
import com.example.contest_service.model.AnswerSubmission;
import com.example.contest_service.model.ContestAttempt;
import com.example.contest_service.repository.AnswerSubmissionRepository;
import com.example.contest_service.repository.ContestAttemptRepository;
import com.example.contest_service.repository.ContestRepository;
import java.util.Map;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.example.contest_service.model.Contest;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.Optional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ContestService {
    private final ContestRepository contestRepository;
    private final ContestAttemptRepository contestAttemptRepository;
    private final WebClient webClient;
    private final QuestionValidator questionValidator;
    private final AnswerSubmissionRepository answerSubmissionRepository;


    public void create(ContestRequest request, String authHeader){
        if (!questionValidator.areValidQuestionIds(request.getQuestionIds(), authHeader)){
            throw new IllegalArgumentException("Some question IDs are invalid");
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

    public void startContest(Long contestId, Long userId){
        Contest contest = contestRepository.findById(contestId).orElseThrow(
                () -> new NoSuchElementException("Contest not found"));
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(contest.getStartTime()) || now.isAfter(contest.getEndTime())) {
            throw new IllegalStateException("Contest in not active");
        }

        Optional<ContestAttempt> existing = contestAttemptRepository.findByUserIdAndContestId(userId, contestId);
        if (existing.isPresent()) {
            return;
        }
        ContestAttempt contestAttempt = new ContestAttempt();
        contestAttempt.setContestId(contestId);
        contestAttempt.setUserId(userId);
        contestAttempt.setStartedAt(now);
        contestAttempt.setCompleted(false);

        contestAttemptRepository.save(contestAttempt);
    }
    public List<QuestionDTO> getQuestionsForContest(Long contestId, String token){
        Contest contest = contestRepository.findById(contestId)
                .orElseThrow(() -> new NoSuchElementException("Contest Not found"));

        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(contest.getStartTime()) || now.isAfter(contest.getEndTime())) {
            throw new IllegalStateException("Contest in not active");
        }


        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<ContestAttempt> existing = contestAttemptRepository.findByUserIdAndContestId(userId, contestId);
        if (existing.isEmpty()) {
            throw new IllegalStateException("User has not started the contest yet");
        }
        if (token == null || !token.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Missing or invalid Authorization token");
        }
        List<UUID> questionIds = contest.getQuestionIds();
        return webClient.post()
                .uri("http://127.0.0.1:8000/api/questions/batch/")
                .header("Authorization", token)
                .bodyValue(Map.of("id_list", questionIds))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<QuestionDTO>>() {
                })
                .block();  // understand the async version

    }
    public void submitAnswer(Long userId,Long contestId, UUID questionId,Long selectedOptionId, String selectedOptionText){

        var answerSubmission = AnswerSubmission.builder() // yes i did add builder annotation to Answer Submission
                .contestId(contestId)
                .userId(userId)
                .questionId(questionId)
                .submittedAt(LocalDateTime.now())
                .selectedOptionId(selectedOptionId)
                .selectedOptionText(selectedOptionText)
                .build();

        answerSubmissionRepository.save(answerSubmission);
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


}

