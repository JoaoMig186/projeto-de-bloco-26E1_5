package car.micro.service;

import car.micro.DTO.AdicionarItemDTO;
import car.micro.DTO.PagamentoIniciadoEvent;
import car.micro.DTO.mock.LojaDTO;
import car.micro.DTO.mock.ProdutoDTO;
import car.micro.DTO.mock.UsuarioDTO;
import car.micro.DTO.response.ItemCarrinhoResponseDTO;
import car.micro.domain.Carrinho;
import car.micro.domain.ENUM.StatusCarrinho;
import car.micro.domain.ItemCarrinho;
import car.micro.metrics.CarrinhoMetrics;
import car.micro.repository.CarrinhoRepository;
import car.micro.service.mock.ProdutoClientMock;
import car.micro.service.mock.UsuarioClientMock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CarrinhoService {
    private final CarrinhoMetrics metrics;
    private final CarrinhoRepository repository;
    private final UsuarioClientMock usuarioClientMock;
    private final ProdutoClientMock produtoClientMock;

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

        LojaDTO loja = produto.loja();
        /*
        ProdutoDTO produto = produtoClient.buscarProduto(dto.produtoId());

        if (produto == null) {
            throw new RuntimeException("Produto não encontrado");
        }

        LojaDTO loja = lojaClient.buscarLoja(produto.lojaId());
        */
        ItemCarrinho item = new ItemCarrinho();
        item.setProdutoId(produto.id());
        item.setLojaId(loja.id());
        item.setNomeProduto(produto.nome());
        item.setPreco(produto.preco());
        item.setQuantidade(dto.quantidade());
        item.setPeso(produto.peso());
        item.setFragil(produto.fragil());
        item.setLatitude(loja.latitude());
        item.setLongitude(loja.longitude());
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

    public PagamentoIniciadoEvent buscarDadosPagamento(Long usuarioId) {
        Carrinho carrinho = repository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new RuntimeException("Carrinho não encontrado"));

        if (carrinho.getItens().isEmpty()) {
            throw new RuntimeException("Carrinho vazio");
        }

        return new PagamentoIniciadoEvent(
                carrinho.getId(),
                carrinho.getUsuarioId(),
                carrinho.getTotal(),
                carrinho.getItens()
                        .stream()
                        .map(item -> new ItemCarrinhoResponseDTO(
                                item.getId(),
                                item.getLojaId(),
                                item.getNomeProduto(),
                                item.getQuantidade(),
                                item.getPeso(),
                                item.getFragil(),
                                new LojaDTO(
                                        item.getLojaId(),
                                        item.getNomeLoja(),
                                        item.getLatitude(),
                                        item.getLongitude()
                                )
                        ))
                        .toList()
        );
    }
}