package car.micro.service.mock;

import car.micro.DTO.request.DeliveryRequestDTO;
import car.micro.DTO.response.DeliveryResponseDTO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class DeliveryClientMock {

    public DeliveryResponseDTO calcularFrete(
            DeliveryRequestDTO request
    ) {

        return new DeliveryResponseDTO(
                new BigDecimal("25.90"),
                3
        );
    }
}