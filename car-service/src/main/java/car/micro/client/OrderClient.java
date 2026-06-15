package car.micro.client;

import car.micro.DTO.PagamentoIniciadoEvent;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

//@FeignClient(name = "order-service")
public interface OrderClient {

//    @PostMapping("/order")
    void criarPedido(
            @RequestBody PagamentoIniciadoEvent dto
    );
}