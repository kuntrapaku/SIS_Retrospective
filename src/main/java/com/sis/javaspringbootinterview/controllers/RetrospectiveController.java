package com.sis.javaspringbootinterview.controllers;

import com.sis.javaspringbootinterview.models.FeedbackItem;
import com.sis.javaspringbootinterview.models.Retrospective;
import com.sis.javaspringbootinterview.repositories.RetrospectiveRepository;
import com.sis.javaspringbootinterview.services.RetrospectiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/retrospectives")
public class RetrospectiveController {

    @Autowired
    private RetrospectiveService retrospectiveService;

    // Create a new retrospective
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<Retrospective> createRetrospective(@RequestBody Retrospective retrospective) {
        if (retrospective.getDate() == null || retrospective.getParticipants().isEmpty()) {
            throw new IllegalArgumentException("Date and participants are required.");
        }
        retrospective.setFeedbackItems(Collections.emptyList());
        return ResponseEntity.status(HttpStatus.CREATED).body(retrospectiveService.createRetrospective(retrospective));
    }

    // Add a feedback item to an existing retrospective
    @PostMapping("/{id}/feedback")
    public ResponseEntity<Retrospective> addFeedbackItem(@PathVariable Long id, @RequestBody FeedbackItem feedbackItem) {
        return ResponseEntity.ok(retrospectiveService.addFeedbackItem(id, feedbackItem));
    }

    // Update a feedback item
    @PutMapping("/{retrospectiveId}/feedback/{feedbackId}")
    public ResponseEntity<Retrospective> updateFeedbackItem(
            @PathVariable Long retrospectiveId,
            @PathVariable Long feedbackId,
            @RequestBody FeedbackItem updatedFeedbackItem) {
        return ResponseEntity.ok(retrospectiveService.updateFeedbackItem(retrospectiveId,feedbackId,updatedFeedbackItem));
    }

    @GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public ResponseEntity<List<Retrospective>> getAllRetrospectives(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "3") int pageSize) {

        List<Retrospective> pageRetrospectives = retrospectiveService.getPageRetrospectives(page, pageSize);

        return ResponseEntity.ok(pageRetrospectives);
    }

    @GetMapping(value = "/search", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public ResponseEntity<List<Retrospective>> searchRetrospectivesByDate(
            @RequestParam("date") @DateTimeFormat(pattern="dd/MM/yyyy") LocalDate date) {

        List<Retrospective> retrospectives = retrospectiveService
                .searchRetrospectivesByDate(date);

        return ResponseEntity.ok(retrospectives);
    }
}
