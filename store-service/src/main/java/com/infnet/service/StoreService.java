package com.infnet.service;

import com.infnet.dtos.GeocodeResponseDTO;
import com.infnet.dtos.StoreRequestDTO;
import com.infnet.dtos.StoreResponseDTO;
import com.infnet.events.StoreCreatedEvent; // Importe o Record do Evento
import com.infnet.kafka.KafkaService; // Importe o Service do Kafka
import com.infnet.model.Store;
import com.infnet.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final KafkaService kafkaService;

    @Transactional
    public StoreResponseDTO createStore(StoreRequestDTO dto) {
        if (storeRepository.findByCnpj(dto.cnpj()).isPresent()) {
            throw new RuntimeException("Já existe uma loja registada com este CNPJ.");
        }

        Store store = new Store();
        store.setName(dto.name());
        store.setCnpj(dto.cnpj());
        store.setAddress(dto.address());
        store.setPhone(dto.phone());
        store.setActive(true);

        // O save() gera o ID no banco, necessário para enviar no evento Kafka
        store = storeRepository.save(store);

        // Enviar evento via Kafka para o geocode-fake-service
        StoreCreatedEvent event = new StoreCreatedEvent(store.getId(), store.getAddress());
        kafkaService.sendStoreCreatedEvent(event);

        return new StoreResponseDTO(store);
    }

    public List<StoreResponseDTO> getAllActiveStores() {
        return storeRepository.findByActiveTrue()
                .stream()
                .map(StoreResponseDTO::new)
                .collect(Collectors.toList());
    }

    public StoreResponseDTO getStoreById(Long id) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Loja não encontrada."));
        return new StoreResponseDTO(store);
    }

    @Transactional
    public void deactivateStore(Long id) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Loja não encontrada."));
        store.setActive(false);
        storeRepository.save(store);
    }

    public GeocodeResponseDTO getStoreGeocode(Long id) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Loja não encontrada."));

        return new GeocodeResponseDTO(store.getLatitude(), store.getLongitude());
    }
}