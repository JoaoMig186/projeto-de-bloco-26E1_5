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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartMetrics metrics;
    private final CartRepository repository;
    private final StoreClient storeClient;

    @Transactional
    public Cart createCart(Long userId) {
        if (repository.findByUserIdAndStatus(userId, CartStatus.OPEN).isPresent()) {
            throw new BusinessException("Usuário já possui um carrinho aberto.");
        }
        Cart cart = new Cart();
        cart.setUserId(userId);
        cart.setStatus(CartStatus.OPEN);

        metrics.incrementarCarrinhosCriados();

        return repository.save(cart);
    }
    @Transactional(readOnly = true)
    public Cart getCart(Long cartId, Long userId) {
        Cart cart = repository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found."));

        if (!cart.getUserId().equals(userId)) {
            throw new AccessDeniedException("Access denied: This cart does not belong to the user.");
        }

        return cart;
    }

    public Cart addItem(Long cartId, Long userId, AddItemDTO dto) {
        Cart cart = getCart(cartId, userId);

        if (cart.getStatus() != CartStatus.OPEN) {
            throw new BusinessException("Cart cannot be modified because it is not open.");
        }

        ProductOrderInfoDTO product = storeClient.getOrderInfo(dto.produtoId());
        if (product == null) {
            throw new ResourceNotFoundException("Product not found.");
        }
        StoreDTO store = product.store();

        if (!cart.getItems().isEmpty()) {
            Long currentStoreId = cart.getItems().get(0).getStoreId();

            if (!currentStoreId.equals(store.id())) {
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

        return repository.save(cart);
    }
    @Transactional
    public Cart removeItem(Long cartId, Long userId, Long itemId) {
        Cart cart = getCart(cartId, userId);

        if (cart.getStatus() != CartStatus.OPEN) {
            throw new BusinessException("Cart cannot be modified because it is not open.");
        }

        cart.getItems().removeIf(item -> item.getProductId().equals(itemId));
        cart.calculateTotal();

        return repository.save(cart);
    }
    @Transactional
    public Cart clearCart(Long cartId, Long userId) {
        Cart cart = getCart(cartId, userId);

        if (cart.getStatus() != CartStatus.OPEN) {
            throw new BusinessException("Cart cannot be modified because it is not open.");
        }

        cart.getItems().clear();
        cart.calculateTotal();

        return repository.save(cart);
    }
    @Transactional
    public Cart finalizeCart(Long cartId, Long userId) {
        Cart cart = getCart(cartId, userId);
        cart.setStatus(CartStatus.CLOSE);
        metrics.incrementarCarrinhosFinalizados();
        return repository.save(cart);
    }
    @Transactional
    public PagamentoIniciadoEvent getPaymentData(Long userId) {
        Cart cart = repository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found."));

        if (cart.getItems().isEmpty()) {
            throw new BusinessException("Cart is empty.");
        }

        if (cart.getStatus() != CartStatus.OPEN) {
            throw new BusinessException("This cart is already being processed or is closed.");
        }

        cart.setStatus(CartStatus.ENVIADOPARAPAGAMENTO);
        repository.save(cart);

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