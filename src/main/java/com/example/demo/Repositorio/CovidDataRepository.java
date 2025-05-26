package com.example.demo.Repositorio;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.CovidData;

public interface CovidDataRepository extends JpaRepository<CovidData, Long> {
}
