package com.example.demo.service;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import weka.classifiers.Classifier;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

import com.example.demo.Repositorio.DiabetesDataRepository;
import com.example.demo.model.DiabetesData;
import com.example.demo.service.dto.PredictionDiabetes;

import java.text.DecimalFormat;
import java.util.logging.Logger;

@Service
public class DiabetesService {

    private static final Logger LOGGER = Logger.getLogger(DiabetesService.class.getName());
    private Classifier classifier;
    private Instances dataStructure;
    private final DiabetesDataRepository repository;

    public DiabetesService(DiabetesDataRepository repository) {
        this.repository = repository;
        try {
            ClassPathResource modelResource = new ClassPathResource("diabetes.model");
            classifier = (Classifier) weka.core.SerializationHelper.read(modelResource.getInputStream());
            LOGGER.info("Modelo diabetes cargado exitosamente.");

            ClassPathResource arffResource = new ClassPathResource("diabetes.arff");
            DataSource source = new DataSource(arffResource.getInputStream());
            dataStructure = source.getDataSet();
            dataStructure.setClassIndex(dataStructure.numAttributes() - 1);
            LOGGER.info("Estructura diabetes.arff cargada exitosamente.");
        } catch (Exception e) {
            LOGGER.severe("Error al inicializar PredictionService: " + e.getMessage());
            throw new RuntimeException("No se pudo inicializar el servicio de predicción", e);
        }
    }

    public PredictionDiabetes predictAndSave(DiabetesData datos) throws Exception {
        LOGGER.info("Datos recibidos: " + datos.toString());

        Instance instance = new DenseInstance(9);
        instance.setDataset(dataStructure);
        instance.setValue(0, datos.getPreg());
        instance.setValue(1, datos.getPlas());
        instance.setValue(2, datos.getPres());
        instance.setValue(3, datos.getSkin());
        instance.setValue(4, datos.getInsu());
        instance.setValue(5, datos.getMass());
        instance.setValue(6, datos.getPedi());
        instance.setValue(7, datos.getAge());

        double predictionValue = classifier.classifyInstance(instance);
        String prediction = dataStructure.classAttribute().value((int) predictionValue);

        double[] probabilities = classifier.distributionForInstance(instance);
        double confidence = probabilities[(int) predictionValue];
        DecimalFormat df = new DecimalFormat("#.#");
        String confidencePercentage = df.format(confidence * 100) + "%";

        datos.setResultadoPrediccion(prediction);
        repository.save(datos);

        return new PredictionDiabetes(prediction, confidencePercentage);
    }

    public void logError(String message, Exception e) {
        LOGGER.severe(message + ": " + e.getMessage());
    }
}