package car.micro.service.mock;

import car.micro.DTO.PagamentoIniciadoEvent;
import car.micro.client.OrderClient;
import org.springframework.stereotype.Service;

@Service
public class OrderClientMock implements OrderClient {

    @Override
    public void criarPedido(PagamentoIniciadoEvent dto) {
        // Aqui nós simulamos o comportamento do serviço externo
        System.out.println("=================================================");
        System.out.println("📦 MOCK ORDER-SERVICE ACIONADO!");
        System.out.println("✅ Recebendo pedido do Carrinho ID: " + dto.carrinhoId());
        System.out.println("👤 Usuário ID: " + dto.usuarioId());
        System.out.println("💰 Valor Total: R$ " + dto.valorTotal());
        System.out.println("🛒 Quantidade de Itens: " + dto.itens().size());
        System.out.println("=================================================");
    }
}