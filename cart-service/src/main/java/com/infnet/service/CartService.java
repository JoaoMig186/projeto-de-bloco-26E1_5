package com.infnet.service;

import com.infnet.DTO.*;
import com.infnet.domain.Cart;
import com.infnet.domain.ENUM.CartStatus;
import com.infnet.domain.CartItem;
import com.infnet.metrics.CartMetrics;
import com.infnet.repository.CartRepository;
import com.infnet.config.exception.BusinessException;
import com.infnet.config.exception.ResourceNotFoundException;
import com.infnet.config.exception.AccessDeniedException;
import com.infnet.config.client.StoreClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartService {

    private final CartMetrics metrics;
    private final CartRepository repository;
    private final StoreClient storeClient;

    @Transactional
    public Cart createCart(Long userId) {
        log.info("[CART]-------Iniciando a criação de um novo carrinho para o usuário: {}", userId);

        if (repository.findByUserIdAndStatus(userId, CartStatus.OPEN).isPresent()) {
            log.warn("[CART]-------Falha ao criar carrinho: Usuário {} já possui um carrinho aberto.", userId);
            throw new BusinessException("Usuário já possui um carrinho aberto.");
        }

        Cart cart = new Cart();
        cart.setUserId(userId);
        cart.setStatus(CartStatus.OPEN);

        metrics.incrementarCarrinhosCriados();

        Cart savedCart = repository.save(cart);
        log.info("[CART]-------Carrinho com ID {} criado com sucesso para o usuário: {}", savedCart.getId(), userId);

        return savedCart;
    }

    @Transactional(readOnly = true)
    public Cart getCart(Long userId) {
        log.debug("[CART]-------Buscando carrinho aberto para o usuário: {}", userId);

        Cart cart = repository.findByUserIdAndStatus(userId, CartStatus.OPEN)
                .orElseThrow(() -> {
                    log.warn("[CART]-------Carrinho não encontrado para o usuário: {}", userId);
                    return new ResourceNotFoundException("Cart not found.");
                });

        if (!cart.getUserId().equals(userId)) {
            log.error("[CART]-------Acesso negado: O carrinho {} não pertence ao usuário {}", cart.getId(), userId);
            throw new AccessDeniedException("Access denied: This cart does not belong to the user.");
        }

        return cart;
    }

    @Transactional
    public Cart addItem(Long userId, AddItemDTO dto) {
        log.info("[CART]-------Adicionando produto ID {} ao carrinho do usuário: {}", dto.produtoId(), userId);
        Cart cart = getCart(userId);

        if (cart.getStatus() != CartStatus.OPEN) {
            log.warn("[CART]-------Tentativa de modificação falhou: O carrinho {} do usuário {} não está aberto.", cart.getId(), userId);
            throw new BusinessException("Cart cannot be modified because it is not open.");
        }

        ProductOrderInfoDTO product = storeClient.getOrderInfo(dto.produtoId());
        if (product == null) {
            log.error("[CART]-------Produto com ID {} não encontrado no StoreClient.", dto.produtoId());
            throw new ResourceNotFoundException("Product not found.");
        }
        StoreDTO store = product.store();

        if (!cart.getItems().isEmpty()) {
            Long currentStoreId = cart.getItems().get(0).getStoreId();

            if (!currentStoreId.equals(store.id())) {
                log.warn("[CART]-------Conflito de lojas: Carrinho do usuário {} é da loja {}, mas o produto é da loja {}.", userId, currentStoreId, store.id());
                throw new BusinessException("Cannot add items from different stores to the same cart. Please clear your cart first.");
            }
        }

        CartItem item = new CartItem();
        item.setProductId(product.itemId());
        item.setStoreId(store.id());
        item.setProductName(product.productName());
        item.setPrice(product.price());
        item.setQuantity(dto.quantidade());
        item.setWeight(product.weight());
        item.setFragile(product.fragile());
        item.setStore(new StoreDTO(
                store.id(),
                store.name(),
                store.latitude(),
                store.longitude()
        ));
        item.setSubtotal(
                item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()))
        );
        cart.getItems().add(item);
        cart.calculateTotal();
        cart.calculateTotalWeight();

        log.info("[CART]-------Produto {} adicionado com sucesso. Total atual do carrinho: {}", product.productName(), cart.getTotal());
        return repository.save(cart);
    }

    @Transactional
    public Cart removeItem(Long userId, Long itemId) {
        log.info("[CART]-------Removendo produto ID {} do carrinho do usuário: {}", itemId, userId);
        Cart cart = getCart(userId);

        if (cart.getStatus() != CartStatus.OPEN) {
            log.warn("[CART]-------Falha ao remover item: O carrinho do usuário {} não está aberto.", userId);
            throw new BusinessException("Cart cannot be modified because it is not open.");
        }

        boolean removed = cart.getItems().removeIf(item -> item.getProductId().equals(itemId));
        if (removed) {
            cart.calculateTotal();
            log.info("[CART]-------Produto ID {} removido. Novo total: {}", itemId, cart.getTotal());
        } else {
            log.debug("[CART]-------Produto ID {} não estava presente no carrinho do usuário {}.", itemId, userId);
        }

        return repository.save(cart);
    }

    @Transactional
    public Cart clearCart(Long userId) {
        log.info("[CART]-------Limpando todos os itens do carrinho do usuário: {}", userId);
        Cart cart = getCart(userId);

        if (cart.getStatus() != CartStatus.OPEN) {
            log.warn("[CART]-------Falha ao limpar: O carrinho do usuário {} não está aberto.", userId);
            throw new BusinessException("Cart cannot be modified because it is not open.");
        }

        cart.getItems().clear();
        cart.calculateTotal();

        log.info("[CART]-------Carrinho do usuário {} esvaziado com sucesso.", userId);
        return repository.save(cart);
    }

    @Transactional
    public Cart finalizeCart(Long userId) {
        log.info("[CART]-------Finalizando o carrinho do usuário: {}", userId);
        Cart cart = getCart(userId);

        cart.setStatus(CartStatus.CLOSE);
        metrics.incrementarCarrinhosFinalizados();

        log.info("[CART]-------Carrinho do usuário {} finalizado (Fechado).", userId);
        return repository.save(cart);
    }

    @Transactional
    public PagamentoIniciadoEvent getPaymentData(Long userId) {
        log.info("[CART]-------Preparando dados de pagamento para o usuário: {}", userId);
        Cart cart = repository.findByUserIdAndStatus(userId, CartStatus.OPEN)
                .orElseThrow(() -> {
                    log.warn("[CART]-------Nenhum carrinho aberto encontrado para pagamento do usuário: {}", userId);
                    return new ResourceNotFoundException("No open cart found for this user.");
                });

        if (cart.getItems().isEmpty()) {
            log.error("[CART]-------Tentativa de pagamento com carrinho vazio para o usuário: {}", userId);
            throw new BusinessException("Cart is empty. Cannot proceed to payment.");
        }

        cart.setStatus(CartStatus.ENVIADOPARAPAGAMENTO);
        repository.save(cart);

        log.info("[CART]-------Carrinho {} do usuário {} enviado para pagamento com sucesso. Total: {}", cart.getId(), userId, cart.getTotal());

        return new PagamentoIniciadoEvent(
                cart.getId(),
                cart.getTotal(),
                cart.getTotalWeightKg(),
                cart.getItems()
                        .stream()
                        .map(item -> new CartItemResponseDTO(
                                item.getProductId(),
                                item.getStoreId(),
                                item.getProductName(),
                                item.getPrice(),
                                item.getWeight(),
                                item.getFragile(),
                                item.getQuantity(),
                                new StoreDTO(
                                        item.getStore().id(),
                                        item.getStore().name(),
                                        item.getStore().latitude(),
                                        item.getStore().longitude()
                                )
                        ))
                        .toList()
        );
    }
}