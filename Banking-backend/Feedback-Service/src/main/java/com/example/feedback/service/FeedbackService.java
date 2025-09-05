package com.example.feedback.service;

import com.example.feedback.entity.Feedback;
import com.example.feedback.repository.FeedbackRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;

    public FeedbackService(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    public Feedback saveFeedback(Feedback feedback) {
        feedback.setTimestamp(LocalDateTime.now());
        return feedbackRepository.save(feedback);
    }
}
