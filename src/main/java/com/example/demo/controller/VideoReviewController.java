package com.example.demo.controller;

import com.example.demo.model.VideoReviewRequest;
import com.example.demo.model.VideoReviewResponse;
import com.example.demo.service.VideoReviewService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
public class VideoReviewController {

    private final VideoReviewService reviewService;

    public VideoReviewController(VideoReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    public VideoReviewResponse reviewVideo(
            @RequestBody VideoReviewRequest request) {

        return reviewService.reviewVideo(request);
    }

    @GetMapping("/health")
    public String health() {
        return "Video Content Reviewer is running";
    }
}
