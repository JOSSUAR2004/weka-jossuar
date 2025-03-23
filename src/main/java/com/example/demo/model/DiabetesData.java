package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;


@Data
@Entity
public class DiabetesData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int preg;
    private int plas;
    private int pres;
    private int skin;
    private int insu;
    private float mass;
    private float pedi;
    private int age;
    private String resultadoPrediccion;
}