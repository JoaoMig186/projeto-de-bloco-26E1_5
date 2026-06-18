package com.infnet.service;

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

//    @Scheduled(fixedDelay = 5000)
//    @Transactional
//    public void publishProductEvents(){
//        List<OutboxProductEvent> events = outboxRepository.findByProcessedFalse();
//
//        for(OutboxProductEvent event : events){
//            kafkaService.sendProductSyncEvent(
//                    event.getPayload()
//            );
//            event.setProcessed(true);
//        }
//    }

}
