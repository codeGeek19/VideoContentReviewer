package com.example.demo.service;

import com.example.demo.model.VideoReviewRequest;
import com.example.demo.model.VideoReviewResponse;
import org.springframework.stereotype.Service;

@Service
public class VideoReviewService {

    public VideoReviewResponse reviewVideo(VideoReviewRequest request) {

        int score = 80;

        if (request.getDuration() > 300) {
            score = 70;
        }

        String feedback;

        if (score >= 80) {
            feedback = "Good overall quality. Minor issues detected.";
        } else {
            feedback = "Video is relatively long. Consider improving engagement.";
        }

        return new VideoReviewResponse(
                request.getVideoName(),
                "REVIEWED",
                request.getDuration(),
                score,
                feedback
        );
    }
}
