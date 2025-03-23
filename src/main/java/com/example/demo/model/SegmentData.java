package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class SegmentData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double regionCentroidCol;
    private double regionCentroidRow;
    private int regionPixelCount;
    private double shortLineDensity5;
    private double shortLineDensity2;
    private double vedgeMean;
    private double vegdeSd;
    private double hedgeMean;
    private double hedgeSd;
    private double intensityMean;
    private double rawredMean;
    private double rawblueMean;
    private double rawgreenMean;
    private double exredMean;
    private double exblueMean;
    private double exgreenMean;
    private double valueMean;
    private double saturationMean;
    private double hueMean;
    private String classPrediction;
}