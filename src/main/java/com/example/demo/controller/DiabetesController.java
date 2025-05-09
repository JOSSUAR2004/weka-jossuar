package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.DiabetesData;
import com.example.demo.service.DiabetesService;
import com.example.demo.service.dto.PredictionDiabetes;

@Controller
@RequestMapping("/prediction")
public class DiabetesController {

    private final DiabetesService predictionService;

    public DiabetesController(DiabetesService predictionService) {
        this.predictionService = predictionService;
    }

    @GetMapping
    public String showForm(Model model) {
        model.addAttribute("diabetesData", new DiabetesData());
        return "Diabetes";
    }

    @PostMapping("/predecir")
    public String predecir(@ModelAttribute DiabetesData datos, Model model) {
        try {
            PredictionDiabetes prediction = predictionService.predictAndSave(datos);

            model.addAttribute("prediccion", prediction.getPrediction());
            model.addAttribute("confianza", prediction.getConfidence());
            model.addAttribute("diabetesData", datos);
            model.addAttribute("showResult", true);

            return "Diabetes";
        } catch (Exception e) {
            predictionService.logError("Error al realizar la predicción", e);
            model.addAttribute("error", "Error interno al procesar la predicción");
            model.addAttribute("datosCita", datos);
            return "Diabetes";
        }
    }
}
