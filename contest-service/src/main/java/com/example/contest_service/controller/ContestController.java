package com.example.contest_service.controller;

import com.example.contest_service.dto.ContestRequest;
import com.example.contest_service.dto.ContestResponse;
import com.example.contest_service.dto.QuestionDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.example.contest_service.service.ContestService;
import java.util.Map;
import java.util.List;

@RestController
@RequiredArgsConstructor
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

    @PostMapping("/start/{id}")
    public ResponseEntity<Map<String, String>> start(@PathVariable Long contestId){
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        contestService.startContest(contestId, userId);
        return ResponseEntity.ok(Map.of("message", "Contest Attempt Started"));
    }

    @GetMapping
    public List<ContestResponse> getAllContests(){
        return contestService.getAllContests();
    }

    @GetMapping("getQuestions/{id}")
    public List<QuestionDTO> getQuestionsForContest(@PathVariable Long contestId,
                                                    @RequestHeader("Authorization") String token ){
        return contestService.getQuestionsForContest(contestId, token);
    }

    @GetMapping("/{id}")
    public ContestResponse getContestById(@PathVariable Long id){
        return contestService.getContestById(id);
    }
}
