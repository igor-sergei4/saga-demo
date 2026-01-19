package com.github.igorsergei4.sagademo.common.microservice.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.igorsergei4.sagademo.common.microservice.EventWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@ConditionalOnProperty(name = "microservice.is-producer", havingValue = "true")
public class QueuedEventService {
    private final Logger LOGGER = LoggerFactory.getLogger(QueuedEventService.class);

    private final QueuedEventRepository queuedEventRepository;

    private final KafkaTemplate<String, EventWrapper<?>> kafkaTemplate;
    private final ObjectMapper customObjectMapper;

    public QueuedEventService(
            QueuedEventRepository queuedEventRepository,
            KafkaTemplate<String, EventWrapper<?>> kafkaTemplate,
            ObjectMapper customObjectMapper
    ) {
        this.queuedEventRepository = queuedEventRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.customObjectMapper = customObjectMapper;
    }

    public void queueAnEvent(String topic, String key, Object payload) {
        String payloadAsString;
        try {
            payloadAsString = customObjectMapper.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        QueuedEvent queuedEvent = new QueuedEvent(
                topic,
                key,
                payload.getClass().getCanonicalName(),
                payloadAsString
        );

        queuedEventRepository.save(queuedEvent);
    }

    @Scheduled(fixedDelay = 5_000)
    public void deliverQueuedEvents() {
        List<QueuedEvent> queuedEvents = queuedEventRepository.findByIsDeliveredFalseOrderByCreatedAtAsc(
                PageRequest.of(0, 100)
        );

        for (QueuedEvent queuedEvent : queuedEvents) {
            try {
                Object payload = customObjectMapper.readValue(
                        queuedEvent.getPayload(),
                        Class.forName(queuedEvent.getPayloadClassName())
                );

                kafkaTemplate.send(
                        queuedEvent.getTopic(),
                        queuedEvent.getKey(),
                        new EventWrapper<>(queuedEvent.getId(), payload)
                ).get();
                queuedEvent.setIsDelivered(true);
                queuedEventRepository.save(queuedEvent);
            }
            catch (Exception e) {
                LOGGER.error("Failed to deliver queued event {} ({})", queuedEvent.getId(), queuedEvent.getTopic(), e);
            }
        }
    }
}
