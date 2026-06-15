package car.micro.service;

import car.micro.DTO.AdicionarItemDTO;
import car.micro.DTO.PagamentoIniciadoEvent;
import car.micro.DTO.mock.LojaDTO;
import car.micro.DTO.mock.ProdutoDTO;
import car.micro.DTO.mock.UsuarioDTO;
import car.micro.DTO.response.ItemCarrinhoResponseDTO;
import car.micro.client.OrderClient;
import car.micro.domain.Carrinho;
import car.micro.domain.ENUM.StatusCarrinho;
import car.micro.domain.ItemCarrinho;
import car.micro.metrics.CarrinhoMetrics;
import car.micro.repository.CarrinhoRepository;
import car.micro.service.mock.LojaClientMock;
import car.micro.service.mock.OrderClientMock;
import car.micro.service.mock.ProdutoClientMock;
import car.micro.service.mock.UsuarioClientMock;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class CarrinhoService {
    private final CarrinhoMetrics metrics;
    private final RedisTemplate<String, Object> redisTemplate;
    private final CarrinhoRepository repository;
    private final UsuarioClientMock usuarioClientMock;
    private final ProdutoClientMock produtoClientMock;
    private final LojaClientMock lojaClientMock;
    private final OrderClientMock orderClient;

    public Carrinho criarCarrinho(Long usuarioId) {
        UsuarioDTO usuario = usuarioClientMock.buscarUsuario(usuarioId);
        if (usuario == null) {
            throw new RuntimeException("Usuário não encontrado");
        }

        Carrinho carrinho = new Carrinho();
        carrinho.setUsuarioId(usuarioId);
        carrinho.setStatus(StatusCarrinho.ABERTO);
        metrics.incrementarCarrinhosCriados();
        return repository.save(carrinho);
    }
    public Carrinho buscarCarrinho(Long carrinhoId, Long usuarioId) {
        Carrinho carrinho = repository.findById(carrinhoId)
                .orElseThrow(() -> new RuntimeException("Carrinho não encontrado"));

        if (!carrinho.getUsuarioId().equals(usuarioId)) {
            throw new RuntimeException("Acesso negado: Este carrinho não pertence ao usuário.");
        }

        return carrinho;
    }

    public Carrinho adicionarItem(Long carrinhoId, Long usuarioId, AdicionarItemDTO dto) {
        Carrinho carrinho = buscarCarrinho(carrinhoId, usuarioId);

        if (carrinho.getStatus() != StatusCarrinho.ABERTO) {
            throw new RuntimeException("Carrinho não pode ser alterado");
        }

        ProdutoDTO produto = produtoClientMock.buscarProduto(dto.produtoId());
        if (produto == null) {
            throw new RuntimeException("Produto não encontrado");
        }

        LojaDTO loja = lojaClientMock.buscarLoja(1L);
        ItemCarrinho item = new ItemCarrinho();
        item.setProdutoId(produto.id());
        item.setLojaId(loja.id());
        item.setNomeProduto(produto.nome());
        item.setPreco(produto.preco());
        item.setQuantidade(dto.quantidade());
        item.setPeso(produto.peso());
        item.setFragil(produto.fragil());
        item.setCepLoja(loja.cep());
        item.setCarrinho(carrinho);
        item.calcularSubtotal();

        carrinho.getItens().add(item);
        carrinho.calcularTotal();

        return repository.save(carrinho);
    }

    public Carrinho removerItem(Long carrinhoId, Long usuarioId, Long itemId) {
        Carrinho carrinho = buscarCarrinho(carrinhoId, usuarioId);

        carrinho.getItens().removeIf(item -> item.getId().equals(itemId));
        carrinho.calcularTotal();

        return repository.save(carrinho);
    }

    public Carrinho limparCarrinho(Long carrinhoId, Long usuarioId) {
        Carrinho carrinho = buscarCarrinho(carrinhoId, usuarioId);

        carrinho.getItens().clear();
        carrinho.calcularTotal();

        return repository.save(carrinho);
    }

    public Carrinho iniciarPagamento(Long carrinhoId, Long usuarioId) {
        Carrinho carrinho = buscarCarrinho(carrinhoId, usuarioId);

        if (carrinho.getItens().isEmpty()) {
            throw new RuntimeException("Carrinho vazio");
        }

        PagamentoIniciadoEvent dto = new PagamentoIniciadoEvent(
                carrinho.getId(),
                carrinho.getUsuarioId(),
                carrinho.getTotal(),
                carrinho.getItens().stream().map(item -> new ItemCarrinhoResponseDTO(
                        item.getId(),
                        item.getLojaId(),
                        item.getNomeProduto(),
                        item.getQuantidade(),
                        item.getPeso(),
                        item.getFragil(),
                        item.getCepLoja()
                )).toList()
        );

        orderClient.criarPedido(dto);

        carrinho.setStatus(StatusCarrinho.ENVIADOPARAPAGAMENTO);
        repository.save(carrinho);

        redisTemplate.opsForValue().set(
                "cart:" + carrinho.getId(),
                carrinho,
                Duration.ofMinutes(30)
        );

        return carrinho;
    }
}