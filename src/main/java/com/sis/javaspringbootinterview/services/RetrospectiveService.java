package com.sis.javaspringbootinterview.services;

import com.sis.javaspringbootinterview.models.FeedbackItem;
import com.sis.javaspringbootinterview.models.Retrospective;
import com.sis.javaspringbootinterview.repositories.RetrospectiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RetrospectiveService {

    @Autowired
    private RetrospectiveRepository retrospectiveRepository;

    public Retrospective createRetrospective(Retrospective retrospective) {
        return retrospectiveRepository.save(retrospective);
    }

    public Retrospective addFeedbackItem(@PathVariable Long id, FeedbackItem feedbackItem) {
        Optional<Retrospective> optionalRetrospective = retrospectiveRepository.findById(id);
        if (optionalRetrospective.isPresent()) {
            Retrospective retrospective = optionalRetrospective.get();
            feedbackItem.setId(null); // Ensure a new ID is generated
            retrospective.getFeedbackItems().add(feedbackItem);
            return retrospectiveRepository.save(retrospective);
        } else {
            throw new IllegalArgumentException("Retrospective not found.");
        }
    }

    public Retrospective updateFeedbackItem(Long retrospectiveId, Long feedbackId, FeedbackItem updatedFeedbackItem) {
        Optional<Retrospective> optionalRetrospective = retrospectiveRepository.findById(retrospectiveId);
        if (optionalRetrospective.isPresent()) {
            Retrospective retrospective = optionalRetrospective.get();
            List<FeedbackItem> feedbackItems = retrospective.getFeedbackItems();
            for (int i = 0; i < feedbackItems.size(); i++) {
                FeedbackItem existingFeedbackItem = feedbackItems.get(i);
                if (existingFeedbackItem.getId().equals(feedbackId)) {
                    existingFeedbackItem.setBody(updatedFeedbackItem.getBody());
                    existingFeedbackItem.setFeedbackType(updatedFeedbackItem.getFeedbackType());
                    return retrospectiveRepository.save(retrospective);
                }
            }
            throw new IllegalArgumentException("Feedback item not found.");
        } else {
            throw new IllegalArgumentException("Retrospective not found.");
        }
    }

    public List<Retrospective> getPageRetrospectives(int page, int pageSize) {
        List<Retrospective> retrospectives = retrospectiveRepository.findAll();
        // Perform pagination
        int startIndex = (page - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, retrospectives.size());

        return retrospectives.subList(startIndex, endIndex);
    }

    public List<Retrospective> searchRetrospectivesByDate(LocalDate date) {
        return retrospectiveRepository.findAll().stream()
                .filter(retro -> retro.getDate().equals(date))
                .collect(Collectors.toList());
    }
}

