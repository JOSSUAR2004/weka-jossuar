package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.SegmentData;
import com.example.demo.service.SegmentService;
import com.example.demo.service.dto.PredictionSegment;

@Controller
@RequestMapping("/segment")
public class SegmentController {

    private final SegmentService segmentService;

    public SegmentController(SegmentService segmentService) {
        this.segmentService = segmentService;
    }

    @GetMapping
    public String showForm(Model model) {
        model.addAttribute("segmentData", new SegmentData());
        return "segment";
    }

    @PostMapping("/predecir")
    public String predecir(@ModelAttribute SegmentData segmentData, Model model) {
        try {
            PredictionSegment prediction = segmentService.predictAndSave(segmentData); 

            model.addAttribute("prediccion", prediction.getPrediction());
            model.addAttribute("confianza", prediction.getConfidence());
            model.addAttribute("segmentData", segmentData);
            model.addAttribute("showResult", true);

            return "segment";
        } catch (Exception e) {
            segmentService.logError("Error al realizar la predicción", e);
            model.addAttribute("error", "Error interno al procesar la predicción");
            model.addAttribute("segmentData", segmentData);
            return "segment";
        }
    }
}