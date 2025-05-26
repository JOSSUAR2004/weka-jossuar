package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity

public class CovidData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    private int age; // Edad en años
    private double bodyTemp; // Temperatura corporal (°C)
    private double oxygenSat; // Saturación de oxígeno (%)
    private int respiratoryRate; // Frecuencia respiratoria (rpm)
    private double lymphocytes; // Nivel de linfocitos (x10^9/L)
    private double crp; // Proteína C reactiva (mg/L)
    private int severity;
    private String confidence;

}
