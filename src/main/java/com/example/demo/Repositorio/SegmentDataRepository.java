package com.example.demo.Repositorio;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.SegmentData;

public interface SegmentDataRepository extends JpaRepository<SegmentData, Long> {
}