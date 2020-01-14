package com.distributedcomputingproject.videomanagementservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    @Autowired
    VideosService videosService;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Value(value = "${KAFKA_MAIN_TOPIC}")
    private String mainTopic;

    @KafkaListener(topics="${KAFKA_MAIN_TOPIC}")
    public void listen(String message) {
        System.out.println("Received message " + message);

        String[] messageParts = message.split("\\|");

        if (messageParts[0].equals("processed")) {
            videosService.setVideoAvailable(Integer.parseInt(messageParts[1]));
        }
        else if (messageParts[0].equals("processingFailed")) {
            videosService.setVideoNotAvailable(Integer.parseInt(messageParts[1]));
        }

    }
}
