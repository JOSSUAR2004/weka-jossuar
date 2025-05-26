package com.example.demo.service;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import weka.classifiers.Classifier;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

import com.example.demo.Repositorio.CovidDataRepository;
import com.example.demo.model.CovidData;
import com.example.demo.service.dto.PredictionCovid;
import java.text.DecimalFormat;
import java.util.logging.Logger;

@Service

public class CovidService {
    private static final Logger LOGGER = Logger.getLogger(CovidService.class.getName());
    private Classifier classifier;
    private Instances dataStructure;
    private final CovidDataRepository repository;

    public CovidService(CovidDataRepository repository) {
        this.repository = repository;

        try {
            // Cargar modelo pre-entrenado
            ClassPathResource modelResource = new ClassPathResource("covid.model");
            classifier = (Classifier) weka.core.SerializationHelper.read(modelResource.getInputStream());
            LOGGER.info("Modelo COVID-19 cargado exitosamente.");

            // Cargar estructura ARFF
            ClassPathResource arffResource = new ClassPathResource("covid19_severity.arff");
            DataSource source = new DataSource(arffResource.getInputStream());
            dataStructure = source.getDataSet();
            dataStructure.setClassIndex(dataStructure.numAttributes() - 1); // severity es la última columna
            LOGGER.info("Estructura covid19_severity.arff cargada exitosamente.");

        } catch (Exception e) {
            LOGGER.severe("Error al inicializar CovidService: " + e.getMessage());
            throw new RuntimeException("No se pudo inicializar el servicio de predicción COVID-19", e);
        }
    }

    public PredictionCovid predictAndSave(CovidData datos) throws Exception {
        LOGGER.info("Datos recibidos para predicción: " + datos.toString());

        // Crear instancia WEKA con los datos recibidos
        Instance instance = new DenseInstance(7); // 6 atributos + clase
        instance.setDataset(dataStructure);

        // Mapear atributos del dataset COVID-19
        instance.setValue(0, datos.getAge());
        instance.setValue(1, datos.getBodyTemp());
        instance.setValue(2, datos.getOxygenSat());
        instance.setValue(3, datos.getRespiratoryRate());
        instance.setValue(4, datos.getLymphocytes());
        instance.setValue(5, datos.getCrp());

        // Realizar predicción
        double predictionValue = classifier.classifyInstance(instance);
        String prediction = dataStructure.classAttribute().value((int) predictionValue);

        // Calcular confianza
        double[] probabilities = classifier.distributionForInstance(instance);
        double confidence = probabilities[(int) predictionValue];
        DecimalFormat df = new DecimalFormat("#.#");
        String confidencePercentage = df.format(confidence * 100) + "%";

        // Guardar predicción y retornar resultado
        datos.setSeverity((int) predictionValue);
        repository.save(datos);

        return new PredictionCovid(
                prediction, confidencePercentage);
    }

    public void logError(String message, Exception e) {
        LOGGER.severe(message + ": " + e.getMessage());
    }

}
