package com.infnet.service;

import com.infnet.DTO.*;

import com.infnet.domain.Cart;
import com.infnet.domain.ENUM.CartStatus;
import com.infnet.domain.CartItem;
import com.infnet.metrics.CarrinhoMetrics;
import com.infnet.repository.CartRepository;
import com.infnet.service.mock.ProdutoClientMock;
import com.infnet.service.mock.UsuarioClientMock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CarService {
    private final CarrinhoMetrics metrics;
    private final CartRepository repository;
    private final UsuarioClientMock usuarioClientMock;
    private final ProdutoClientMock produtoClientMock;

    public Cart createCart(Long usuarioId) {
        UserDTO usuario = usuarioClientMock.buscarUsuario(usuarioId);
        if (usuario == null) {
            throw new RuntimeException("Usuário não encontrado");
        }

        Cart carrinho = new Cart();
        carrinho.setUserId(usuarioId);
        carrinho.setStatus(CartStatus.OPEN);
        metrics.incrementarCarrinhosCriados();
        return repository.save(carrinho);
    }
    public Cart getCart(Long carrinhoId, Long usuarioId) {
        Cart carrinho = repository.findById(carrinhoId)
                .orElseThrow(() -> new RuntimeException("Carrinho não encontrado"));

        if (!carrinho.getUserId().equals(usuarioId)) {
            throw new RuntimeException("Acesso negado: Este carrinho não pertence ao usuário.");
        }

        return carrinho;
    }

    public Cart addItem(Long carrinhoId, Long usuarioId, AddItemDTO dto) {
        Cart carrinho = getCart(carrinhoId, usuarioId);
        if (carrinho.getStatus() != CartStatus.OPEN) {
            throw new RuntimeException("Carrinho não pode ser alterado");
        }
        ProductOrderInfoDTO produto = produtoClientMock.buscarProduto(dto.produtoId());

        if (produto == null) {
            throw new RuntimeException("Produto não encontrado");
        }

        StoreDTO loja = produto.store();
        CartItem item = new CartItem();
        item.setProductId(produto.itemId());
        item.setStoreId(loja.id());
        item.setProductName(produto.productName());
        item.setPrice(produto.preco());
        item.setQuantity(dto.quantidade());
        item.setWeight(produto.weight());
        item.setFragile(produto.fragile());
        item.setLatitude(loja.latitude());
        item.setLongitude(loja.longitude());
        item.calculateSubtotal();
        carrinho.getItems().add(item);
        carrinho.calculateTotal();

        return repository.save(carrinho);
    }

    public Cart removeItem(Long carrinhoId, Long usuarioId, Long itemId) {
        Cart carrinho = getCart(carrinhoId, usuarioId);

        carrinho.getItems().removeIf(item -> item.getProductId().equals(itemId));
        carrinho.calculateTotal();
        return repository.save(carrinho);
    }

    public Cart clearCart(Long carrinhoId, Long usuarioId) {
        Cart carrinho = getCart(carrinhoId, usuarioId);

        carrinho.getItems().clear();
        carrinho.calculateTotal();

        return repository.save(carrinho);
    }

    public PagamentoIniciadoEvent buscarDadosPagamento(Long usuarioId) {
        Cart carrinho = repository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new RuntimeException("Carrinho não encontrado"));

        if (carrinho.getItems().isEmpty()) {
            throw new RuntimeException("Carrinho vazio");
        }

        return new PagamentoIniciadoEvent(
                carrinho.getId(),
                carrinho.getTotal(),
                carrinho.getTotalWeightKg(),
                carrinho.getItems()
                        .stream()
                        .map(item -> new CartItemResponseDTO(
                                item.getProductId(),
                                item.getStoreId(),
                                item.getProductName(),
                                item.getQuantity(),
                                item.getWeight(),
                                item.getFragile(),
                                new StoreDTO(
                                        item.getStoreId(),
                                        item.getStoreName(),
                                        item.getLatitude(),
                                        item.getLongitude()
                                )
                        ))
                        .toList()
        );
    }
}