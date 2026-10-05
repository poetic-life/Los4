package com.laclippers.los.controller;

import com.laclippers.los.common.Result;
import com.laclippers.los.entity.Feedback;
import com.laclippers.los.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/feedbacks")
public class FeedbackController {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @PostMapping
    public Result<Feedback> submit(@RequestBody Feedback feedback) {
        feedback.setId(null);
        feedback.setCreatedAt(LocalDateTime.now());
        return Result.ok(feedbackRepository.save(feedback));
    }
}