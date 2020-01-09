package com.distributedcomputingproject.videoprocessingservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class KafkaProducerService {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Value(value="${KAFKA_MAIN_TOPIC}")
    private String maintopic;

    public void sendProcessedMessage(Integer videoId){
        kafkaTemplate.send(maintopic, "processed|" + videoId);
    }
    public void sendProcessingFailedMessage(Integer videoId){
        kafkaTemplate.send(maintopic, "processingFailed|" + videoId);
    }
}
