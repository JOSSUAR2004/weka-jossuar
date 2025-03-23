package com.example.demo.service.dto;

public class PredictionSegment {
    private final String prediction;
    private final String confidence;

    public PredictionSegment(String prediction, String confidence) {
        this.prediction = prediction;
        this.confidence = confidence;
    }

    public String getPrediction() {
        return prediction;
    }

    public String getConfidence() {
        return confidence;
    }
}