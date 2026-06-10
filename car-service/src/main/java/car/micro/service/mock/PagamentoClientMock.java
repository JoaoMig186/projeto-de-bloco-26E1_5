package car.micro.service.mock;

import org.springframework.stereotype.Service;

@Service
public class PagamentoClientMock {

    public PaymentResponseDTO pagar(
            PaymentRequestDTO request
    ) {

        return new PaymentResponseDTO(
                "APROVADO",
                "TXN-123456"
        );
    }
}