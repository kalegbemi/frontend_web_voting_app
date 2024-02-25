package com.capstone_project.web_voting_app.controller;

import com.capstone_project.web_voting_app.service.ElectionResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/electionResult")
public class ElectionResultController {

    private final ElectionResultService electionResultService;
@GetMapping("/totalresult/{candidateId}/{electionId}")
    public Map<String, Serializable> getTotalResult(@PathVariable long candidateId, @PathVariable long electionId) {
        return electionResultService.getTotalResult(candidateId,electionId);
    }
@GetMapping("/candidateresult/{electionId}")
    public Map<String, Serializable> getTotalResultByCandidate(@PathVariable long electionId) {
    return electionResultService.getTotalResultByCandidate(electionId);
    }
}