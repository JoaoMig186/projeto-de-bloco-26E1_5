package com.infnet.repository;

import com.infnet.model.outbox.OutboxProductEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OutboxProductEventRepository extends JpaRepository<OutboxProductEvent, UUID> {
    List<OutboxProductEvent> findByProcessedFalse();
}
