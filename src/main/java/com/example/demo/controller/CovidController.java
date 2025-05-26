package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.demo.model.CovidData;
import com.example.demo.service.CovidService;
import com.example.demo.service.dto.PredictionCovid;

@Controller
@RequestMapping("/covid")

public class CovidController {

    private final CovidService covidService;

    public CovidController(CovidService covidService) {
        this.covidService = covidService;
    }

    @GetMapping
    public String showForm(Model model) {
        model.addAttribute("covidData", new CovidData());
        return "Covid";
    }

    @PostMapping("/predecir")
    public String predecir(@ModelAttribute CovidData covidData, Model model) {
        try {

            System.out.println(covidData.toString());
            PredictionCovid prediction = covidService.predictAndSave(covidData);
            covidData.setConfidence(prediction.getConfidence());
            model.addAttribute(prediction.getPrediction()); // Devuelve 0,
            model.addAttribute("covidData", covidData);
            model.addAttribute("showResult", true);
            return "Covid";
        } catch (Exception e) {
            model.addAttribute("error", "Error processing COVID data: " + e.getMessage());
            return "Covid";
        }
    }
}
