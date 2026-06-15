package car.micro.config;

import car.micro.domain.Carrinho;
import car.micro.domain.ENUM.StatusCarrinho;
import car.micro.repository.CarrinhoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CarrinhoCleanupJob {

    private final CarrinhoRepository repository;

    @Scheduled(fixedRate = 60000)
    public void limparCarrinhosExpirados() {

        LocalDateTime limite =
                LocalDateTime.now().minusMinutes(30);

        List<Carrinho> carrinhos =
                repository.findByStatusAndDataAtualizacaoBefore(
                        StatusCarrinho.ENVIADOPARAPAGAMENTO,
                        limite
                );

        repository.deleteAll(carrinhos);
    }
}