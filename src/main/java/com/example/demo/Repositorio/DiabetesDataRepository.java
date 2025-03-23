package com.example.demo.Repositorio;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.DiabetesData;

public interface DiabetesDataRepository extends JpaRepository<DiabetesData, Long> {
}