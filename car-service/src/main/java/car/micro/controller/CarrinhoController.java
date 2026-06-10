package car.micro.controller;

import car.micro.DTO.request.AdicionarItemDTO;
import car.micro.DTO.response.DeliveryResponseDTO;
import car.micro.DTO.response.PaymentResponseDTO;
import car.micro.domain.Carrinho;
import car.micro.domain.ItemCarrinho;
import car.micro.service.CarrinhoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/carrinhos")
@RequiredArgsConstructor
public class CarrinhoController {

    private final CarrinhoService service;

    @PostMapping("/usuario/{usuarioId}")
    public ResponseEntity<Carrinho> criar(
            @PathVariable Long usuarioId
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.criarCarrinho(usuarioId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Carrinho> buscar(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(service.buscarCarrinho(id));
    }

    @PostMapping("/{id}/itens")
    public ResponseEntity<Carrinho> adicionarItem(
            @PathVariable Long id,
            @RequestBody AdicionarItemDTO dto
    ) {
        return ResponseEntity.ok(
                service.adicionarItem(id, dto)
        );
    }

    @DeleteMapping("/{id}/itens/{itemId}")
    public ResponseEntity<Carrinho> removerItem(
            @PathVariable Long id,
            @PathVariable Long itemId
    ) {
        return ResponseEntity.ok(
                service.removerItem(id, itemId)
        );
    }

    @DeleteMapping("/{id}/limpar")
    public ResponseEntity<Carrinho> limpar(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                service.limparCarrinho(id)
        );
    }

    @GetMapping("/{id}/delivery")
    public ResponseEntity<DeliveryResponseDTO> calcularFrete(
            @PathVariable Long id,
            @RequestParam String cepEntrega
    ) {
        return ResponseEntity.ok(
                service.calcularFrete(id, cepEntrega)
        );
    }

    @PostMapping("/{id}/pagamento")
    public ResponseEntity<PaymentResponseDTO> realizarPagamento(
            @PathVariable Long id,
            @RequestParam BigDecimal frete
    ) {
        return ResponseEntity.ok(
                service.realizarPagamento(id, frete)
        );
    }
}