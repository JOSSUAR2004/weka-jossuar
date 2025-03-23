package com.example.demo.service;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import weka.classifiers.Classifier;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

import com.example.demo.Repositorio.SegmentDataRepository;
import com.example.demo.model.SegmentData;
import com.example.demo.service.dto.PredictionSegment;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.logging.Logger;

@Service
public class SegmentService {

    private static final Logger LOGGER = Logger.getLogger(SegmentService.class.getName());
    private Classifier classifier;
    private Instances dataStructure;
    private final SegmentDataRepository repository;

    public SegmentService(SegmentDataRepository repository) {
        this.repository = repository;
        try {
            ClassPathResource modelResource = new ClassPathResource("segment.model");
            classifier = (Classifier) weka.core.SerializationHelper.read(modelResource.getInputStream());
            LOGGER.info("Modelo segment cargado exitosamente.");

            ClassPathResource arffResource = new ClassPathResource("segment.arff");
            DataSource source = new DataSource(arffResource.getInputStream());
            dataStructure = source.getDataSet();
            dataStructure.setClassIndex(dataStructure.numAttributes() - 1);
            LOGGER.info("Estructura segment.arff cargada exitosamente.");
        } catch (Exception e) {
            LOGGER.severe("Error al inicializar SegmentService: " + e.getMessage());
            throw new RuntimeException("No se pudo inicializar el servicio de segmentación", e);
        }
    }

    public PredictionSegment predictAndSave(SegmentData segmentData) throws Exception {  
        LOGGER.info("Datos recibidos: " + segmentData.toString());

        Instance instance = new DenseInstance(20);
        instance.setDataset(dataStructure);
        instance.setValue(0, segmentData.getRegionCentroidCol());
        instance.setValue(1, segmentData.getRegionCentroidRow());
        instance.setValue(2, segmentData.getRegionPixelCount());
        instance.setValue(3, segmentData.getShortLineDensity5());
        instance.setValue(4, segmentData.getShortLineDensity2());
        instance.setValue(5, segmentData.getVedgeMean());
        instance.setValue(6, segmentData.getVegdeSd());
        instance.setValue(7, segmentData.getHedgeMean());
        instance.setValue(8, segmentData.getHedgeSd());
        instance.setValue(9, segmentData.getIntensityMean());
        instance.setValue(10, segmentData.getRawredMean());
        instance.setValue(11, segmentData.getRawblueMean());
        instance.setValue(12, segmentData.getRawgreenMean());
        instance.setValue(13, segmentData.getExredMean());
        instance.setValue(14, segmentData.getExblueMean());
        instance.setValue(15, segmentData.getExgreenMean());
        instance.setValue(16, segmentData.getValueMean());
        instance.setValue(17, segmentData.getSaturationMean());
        instance.setValue(18, segmentData.getHueMean());

        double predictionValue = classifier.classifyInstance(instance);
        String prediction = dataStructure.classAttribute().value((int) predictionValue);

        double[] probabilities = classifier.distributionForInstance(instance);
        double confidence = probabilities[(int) predictionValue];
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        DecimalFormat df = new DecimalFormat("#.##", symbols);
        String confidencePercentage = df.format(confidence * 100) + "%";

        segmentData.setClassPrediction(prediction);
        repository.save(segmentData);

        return new PredictionSegment(prediction, confidencePercentage); 
    }

    public void logError(String message, Exception e) {
        LOGGER.severe(message + ": " + e.getMessage());
    }
}