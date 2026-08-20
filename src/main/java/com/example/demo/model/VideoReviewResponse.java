package com.example.demo.model;

public class VideoReviewResponse {

    private String videoName;
    private String status;
    private int duration;
    private int score;
    private String feedback;

    public VideoReviewResponse(String videoName, String status,
                               int duration, int score, String feedback) {
        this.videoName = videoName;
        this.status = status;
        this.duration = duration;
        this.score = score;
        this.feedback = feedback;
    }

    public String getVideoName() {
        return videoName;
    }

    public String getStatus() {
        return status;
    }

    public int getDuration() {
        return duration;
    }

    public int getScore() {
        return score;
    }

    public String getFeedback() {
        return feedback;
    }
}
