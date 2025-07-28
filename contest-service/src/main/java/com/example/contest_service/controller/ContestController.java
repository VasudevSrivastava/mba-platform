package com.example.contest_service.controller;

import com.example.contest_service.dto.AnswerSubmissionRequest;
import com.example.contest_service.dto.ContestRequest;
import com.example.contest_service.dto.ContestResponse;
import com.example.contest_service.dto.QuestionDTO;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.example.contest_service.service.ContestService;
import java.util.Map;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/contests")
public class ContestController {

    private final ContestService contestService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> create(@RequestBody @Valid ContestRequest request,
                                         @RequestHeader("Authorization") String authHeader){
        contestService.create(request, authHeader);
        return ResponseEntity.ok("Contest Created");
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> update(@RequestBody @Valid ContestRequest updatedContest, @PathVariable Long id){
        contestService.update(id, updatedContest);
        return ResponseEntity.ok("Contest Updated");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> delete(@PathVariable Long id){
        contestService.delete(id);
        return ResponseEntity.ok("Contest Deleted");
    }

    @PostMapping("/{contestId}/start")
    public ResponseEntity<Map<String, String>> start(@PathVariable Long contestId){
        Object principalObj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Long userId;
        if (principalObj instanceof Long) {
            userId = (Long) principalObj;
        } else if (principalObj instanceof String) {
            userId = Long.parseLong((String) principalObj);  // safe parse
        } else {
            throw new IllegalStateException("Unexpected principal type: " + principalObj.getClass());
        }

        contestService.startContest(contestId, userId);
        return ResponseEntity.ok(Map.of("message", "Contest Attempt Started"));
    }

    @GetMapping
    public List<ContestResponse> getAllContests(){
        return contestService.getAllContests();
    }

    @GetMapping("{contestId}/getQuestions")
    public List<QuestionDTO> getQuestionsForContest(@PathVariable Long contestId,
                                                    @RequestHeader("Authorization") String token ){
        return contestService.getQuestionsForContest(contestId, token);
    }
    @PostMapping("{contest_id}/submit-answer")
    public ResponseEntity<Map<String, String>> submitAnswer(@PathVariable Long contestId, @RequestBody AnswerSubmissionRequest request){
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        contestService.submitAnswer(userId, contestId, request.getQuestionId(), request.getOptionId(), request.getOptionText());
        return ResponseEntity.ok(Map.of("message", "Answer Submitted Successfully"));

    }

    @GetMapping("/{id}")
    public ContestResponse getContestById(@PathVariable Long id){
        return contestService.getContestById(id);
    }
}
