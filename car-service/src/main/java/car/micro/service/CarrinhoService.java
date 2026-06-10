package car.micro.service;

import car.micro.DTO.mock.LojaDTO;
import car.micro.DTO.mock.ProdutoDTO;
import car.micro.DTO.mock.UsuarioDTO;
import car.micro.DTO.request.AdicionarItemDTO;
import car.micro.domain.Carrinho;
import car.micro.domain.ENUM.StatusCarrinho;
import car.micro.domain.ItemCarrinho;
import car.micro.repository.CarrinhoRepository;
import car.micro.service.mock.LojaClientMock;
import car.micro.service.mock.ProdutoClientMock;
import car.micro.service.mock.UsuarioClientMock;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class CarrinhoService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final CarrinhoRepository repository;
    private final UsuarioClientMock usuarioClientMock;
    private final ProdutoClientMock produtoClientMock;
    private final LojaClientMock lojaClientMock;

    public Carrinho criarCarrinho(Long usuarioId) {

        UsuarioDTO usuario =
                usuarioClientMock.buscarUsuario(usuarioId);

        if (usuario == null) {
            throw new RuntimeException("Usuário não encontrado");
        }

        Carrinho carrinho = new Carrinho();

        carrinho.setUsuarioId(usuarioId);
        carrinho.setStatus(StatusCarrinho.ABERTO);

        return repository.save(carrinho);
    }

    public Carrinho buscarCarrinho(Long carrinhoId) {

        return repository.findById(carrinhoId)
                .orElseThrow(() ->
                        new RuntimeException("Carrinho não encontrado"));
    }

    public Carrinho adicionarItem(
            Long carrinhoId,
            AdicionarItemDTO dto
    ) {

        Carrinho carrinho = buscarCarrinho(carrinhoId);

        if (carrinho.getStatus() != StatusCarrinho.ABERTO) {
            throw new RuntimeException(
                    "Carrinho não pode ser alterado"
            );
        }

        ProdutoDTO produto =
                produtoClientMock.buscarProduto(dto.produtoId());

        if (produto == null) {
            throw new RuntimeException("Produto não encontrado");
        }

        LojaDTO loja =
                lojaClientMock.buscarLoja(1L);

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

    public Carrinho removerItem(
            Long carrinhoId,
            Long itemId
    ) {

        Carrinho carrinho = buscarCarrinho(carrinhoId);

        carrinho.getItens()
                .removeIf(item ->
                        item.getId().equals(itemId));

        carrinho.calcularTotal();

        return repository.save(carrinho);
    }

    public Carrinho limparCarrinho(Long carrinhoId) {

        Carrinho carrinho = buscarCarrinho(carrinhoId);

        carrinho.getItens().clear();

        carrinho.calcularTotal();

        return repository.save(carrinho);
    }

    public Carrinho iniciarCheckout(Long carrinhoId) {

        Carrinho carrinho = buscarCarrinho(carrinhoId);

        if (carrinho.getItens().isEmpty()) {
            throw new RuntimeException(
                    "Carrinho vazio"
            );
        }

        carrinho.setStatus(
                StatusCarrinho.AGUARDANDO_PAGAMENTO
        );

        repository.save(carrinho);
        redisTemplate.opsForValue().set(
                "cart:" + carrinho.getId(),
                carrinho,
                Duration.ofMinutes(30)
        );
        /*
         * Aqui você publicaria o evento para o Order:
         *
         * CheckoutIniciadoEvent event =
         *      new CheckoutIniciadoEvent(
         *          carrinho.getId(),
         *          carrinho.getUsuarioId(),
         *          carrinho.getTotal(),
         *          carrinho.getItens()
         *      );
         *
         * kafkaTemplate.send(
         *      "checkout-iniciado",
         *      event
         * );
         */

        return carrinho;
    }

    public void checkoutAprovado(Long carrinhoId) {

        Carrinho carrinho =
                buscarCarrinho(carrinhoId);

        carrinho.setStatus(
                StatusCarrinho.PAGO
        );

        repository.save(carrinho);

        redisTemplate.delete(
                "cart:" + carrinhoId
        );

        repository.delete(carrinho);
    }

    public void checkoutRecusado(Long carrinhoId) {

        Carrinho carrinho =
                buscarCarrinho(carrinhoId);

        carrinho.setStatus(
                StatusCarrinho.ABERTO
        );

        repository.save(carrinho);
        redisTemplate.delete(
                "cart:" + carrinhoId
        );
    }
}