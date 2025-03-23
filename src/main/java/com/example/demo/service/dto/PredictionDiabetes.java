
package com.example.demo.service.dto;

public class PredictionDiabetes {

private final String prediction;
private final String confidence;

public PredictionDiabetes(String prediction, String confidence) {
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