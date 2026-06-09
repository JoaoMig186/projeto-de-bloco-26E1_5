package car.micro.service;

import car.micro.DTO.ItemDeliveryDTO;
import car.micro.DTO.mock.LojaDTO;
import car.micro.DTO.mock.ProdutoDTO;
import car.micro.DTO.mock.UsuarioDTO;
import car.micro.DTO.request.AdicionarItemDTO;
import car.micro.DTO.request.DeliveryRequestDTO;

import car.micro.DTO.request.PaymentRequestDTO;

import car.micro.DTO.response.DeliveryResponseDTO;
import car.micro.DTO.response.PaymentResponseDTO;
import car.micro.domain.Carrinho;
import car.micro.domain.ItemCarrinho;
import car.micro.repository.CarrinhoRepository;
import car.micro.service.mock.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CarrinhoService {

    private final CarrinhoRepository repository;
    //times externos mocados (por enquanto)
    private final UsuarioClientMock usuarioClientMock;
    private final ProdutoClientMock produtoClientMock;
    private final LojaClientMock lojaClientMock;
    private final DeliveryClientMock deliveryClientMock;
    private final PagamentoClientMock pagamentoClientMock;

    public Carrinho criarCarrinho(Long usuarioId) {

        UsuarioDTO usuario =
                usuarioClientMock.buscarUsuario(usuarioId);

        if (usuario == null) {
            throw new RuntimeException("Usuário não encontrado");
        }

        Carrinho carrinho = new Carrinho();
        carrinho.setUsuarioId(usuarioId);

        return repository.save(carrinho);
    }

    public Carrinho buscarCarrinho(Long carrinhoId) {
        return repository.findById(carrinhoId)
                .orElseThrow(() -> new RuntimeException("Carrinho não encontrado"));
    }

    public Carrinho adicionarItem(
            Long carrinhoId,
            AdicionarItemDTO dto
    ) {

        Carrinho carrinho = buscarCarrinho(carrinhoId);

        ProdutoDTO produto =
                produtoClientMock.buscarProduto(dto.produtoId());

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

        item.setSubtotal(
                produto.preco().multiply(
                        BigDecimal.valueOf(dto.quantidade())
                )
        );

        item.setCarrinho(carrinho);

        carrinho.getItens().add(item);

        carrinho.calcularTotal();

        return repository.save(carrinho);
    }

    public Carrinho removerItem(Long carrinhoId, Long itemId) {
        Carrinho carrinho = buscarCarrinho(carrinhoId);

        carrinho.getItens().removeIf(item -> item.getId().equals(itemId));

        carrinho.calcularTotal();

        return repository.save(carrinho);
    }

    public Carrinho limparCarrinho(Long carrinhoId) {
        Carrinho carrinho = buscarCarrinho(carrinhoId);

        carrinho.getItens().clear();

        carrinho.calcularTotal();

        return repository.save(carrinho);
    }

    public DeliveryRequestDTO gerarDeliveryRequest(Long carrinhoId, String cepEntrega) {
        Carrinho carrinho = buscarCarrinho(carrinhoId);

        return new DeliveryRequestDTO(
                carrinho.getId(),
                carrinho.getUsuarioId(),
                carrinho.getTotal(),
                cepEntrega,
                carrinho.getItens()
                        .stream()
                        .map(item -> new ItemDeliveryDTO(
                                item.getProdutoId(),
                                item.getLojaId(),
                                item.getCepLoja(),
                                item.getQuantidade(),
                                item.getPeso(),
                                item.getFragil()
                        ))
                        .toList()
        );
    }
    public DeliveryResponseDTO calcularFrete(
            Long carrinhoId,
            String cepEntrega
    ) {

        DeliveryRequestDTO request =
                gerarDeliveryRequest(carrinhoId, cepEntrega);

        return deliveryClientMock.calcularFrete(request);
    }

    public PaymentResponseDTO realizarPagamento(
            Long carrinhoId,
            BigDecimal frete
    ) {

        PaymentRequestDTO request =
                gerarPaymentRequest(carrinhoId, frete);

        PaymentResponseDTO response =
                pagamentoClientMock.pagar(request);

        confirmarPagamento(
                carrinhoId,
                response.status()
        );

        return response;
    }

    public PaymentRequestDTO gerarPaymentRequest(Long carrinhoId, BigDecimal frete) {
        Carrinho carrinho = buscarCarrinho(carrinhoId);
        carrinho.calcularTotal();
        BigDecimal valorFinal = carrinho.getTotal().add(frete);

        return new PaymentRequestDTO(
                carrinho.getId(),
                carrinho.getUsuarioId(),
                valorFinal
        );
    }
    public void confirmarPagamento(Long carrinhoId, String status) {
        Carrinho carrinho = buscarCarrinho(carrinhoId);

        if ("APROVADO".equalsIgnoreCase(status)) {

            carrinho.getItens().clear();
            carrinho.calcularTotal();

            repository.save(carrinho);

            System.out.println("SUCESSO: Carrinho " + carrinhoId + " limpo após pagamento aprovado.");
        } else {
            System.out.println("AVISO: Pagamento não aprovado para o carrinho " + carrinhoId + ". Status: " + status);
        }
    }
}
