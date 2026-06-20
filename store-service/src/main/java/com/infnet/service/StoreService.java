package com.infnet.service;

import com.infnet.dtos.StoreRequestDTO;
import com.infnet.dtos.StoreResponseDTO;
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

    @Transactional
    public StoreResponseDTO createStore(StoreRequestDTO dto) {
        // Regra de negócio: Verificar se o CNPJ já existe
        if (storeRepository.findByCnpj(dto.cnpj()).isPresent()) {
            throw new RuntimeException("Já existe uma loja registada com este CNPJ.");
        }

        Store store = new Store();
        store.setName(dto.name());
        store.setCnpj(dto.cnpj());
        store.setAddress(dto.address());
        store.setPhone(dto.phone());
        store.setActive(true);

        store = storeRepository.save(store);
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
        store.setActive(false); // Inativação lógica (soft delete) em vez de apagar do banco
        storeRepository.save(store);
    }
}