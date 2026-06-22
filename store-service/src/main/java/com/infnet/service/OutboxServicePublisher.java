package com.infnet.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.infnet.dtos.ProductSyncDTO;
import com.infnet.kafka.KafkaService;
import com.infnet.model.outbox.OutboxProductEvent;
import com.infnet.repository.OutboxProductEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OutboxServicePublisher {

    private final OutboxProductEventRepository outboxRepository;
    private final KafkaService kafkaService;
    private final ObjectMapper objectMapper;

    @Scheduled(fixedDelay = 5000)
    @Transactional
    public void publishProductEvents() throws JsonProcessingException {
        List<OutboxProductEvent> events = outboxRepository.findByProcessedFalse();

        for(OutboxProductEvent event : events){
            ProductSyncDTO dto = objectMapper.readValue(
                    event.getPayload(),
                    ProductSyncDTO.class
            );

            kafkaService.sendProductSyncEvent(dto);

            event.setProcessed(true);
        }
    }

}
