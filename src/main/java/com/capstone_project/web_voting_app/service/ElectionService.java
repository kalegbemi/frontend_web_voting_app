package com.capstone_project.web_voting_app.service;

import com.capstone_project.web_voting_app.dto.ElectionRequest;
import com.capstone_project.web_voting_app.dto.HttpResponse;
import com.capstone_project.web_voting_app.enom.Status;
import com.capstone_project.web_voting_app.exception.ElectionNotFoundException;
import com.capstone_project.web_voting_app.model.Election;
import com.capstone_project.web_voting_app.repository.ElectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ElectionService {

    private final ElectionRepository electionRepo;

    public ResponseEntity<String> findElectionById(Long id) {
        Election election = electionRepo.findById(id).orElseThrow(
                ()->new ElectionNotFoundException("There is no record of election with this id."));

        return ResponseEntity.ok("Election created successfully\n"+election.getTitle());
    }

    @Transactional
    public void updateElectionStatusBaseOnDate() {
        electionRepo.updateElectionStatusBaseOnDates();
    }

    public ResponseEntity<List<Election>> findElectionByStatus(Status status) {
        List<Election> election = electionRepo.findByStatus(status);
        return ResponseEntity.ok(election);
    }

    public ResponseEntity<Election> findElectionByTitle(String title) {
        Election election = electionRepo.findByTitle(title);
        return ResponseEntity.ok(election);
    }

    /*public ResponseEntity<ElectionPageableResponse> findAllElection(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Election> electionPage = electionRepo.findAll(pageable);
        List<Election> electionList = electionPage.getContent();

        ElectionPageableResponse EPR = ElectionPageableResponse.builder()
                .content(electionList)
                .pageNo(electionPage.getNumber())
                .pageSize(electionPage.getSize())
                .totalPages(electionPage.getTotalPages())
                .totalSize((int) electionPage.getTotalElements())
                .last(electionPage.isLast())
                .build();

        return ResponseEntity.ok(EPR);
    }*/
    public List<Election> findAllElection() {
       List<Election> elections = electionRepo.findAll();
       return elections;

    }

    public ResponseEntity<Election> updateElectionById(Long id, ElectionRequest request) {
        Election election2Update = electionRepo.findById(id).orElseThrow(
                ()->new ElectionNotFoundException("election not found"));
        election2Update.setTitle(request.getTitle());
        election2Update.setStartDate(request.getStartDate());
        election2Update.setEndDate(request.getEndDate());

        return ResponseEntity.ok(electionRepo.save(election2Update));

    }

    public ResponseEntity<Election> createElection(ElectionRequest request) {
        Election election = new Election();

        election.setTitle(request.getTitle());
        election.setStartDate(request.getStartDate());
        election.setEndDate(request.getEndDate());

        electionRepo.save(election);

        return new ResponseEntity<>(election, HttpStatusCode.valueOf(200));
    }

    public ResponseEntity<HttpResponse> deleteElectionById(Long id) {
        if (!electionRepo.existsById(id)){
            throw new ElectionNotFoundException("Election not found");
        }

        electionRepo.deleteById(id);
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .status(HttpStatus.OK.name())
                        .requestMethod("getMapping")
                        .statusCode(HttpStatus.OK.value())
                        .message("Election with ID "+id+", has been successfully deleted")
                        .build()
        );
    }
}
