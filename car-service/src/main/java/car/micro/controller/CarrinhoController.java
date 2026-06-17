package car.micro.controller;

import car.micro.DTO.AdicionarItemDTO;
import car.micro.DTO.PagamentoIniciadoEvent;
import car.micro.domain.Carrinho;
import car.micro.service.CarrinhoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carrinhos")
@RequiredArgsConstructor
public class CarrinhoController {

    private final CarrinhoService service;

    @PostMapping
    public ResponseEntity<Carrinho> criar(
            @RequestHeader("X-User-Id") Long usuarioId
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.criarCarrinho(usuarioId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Carrinho> buscar(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long usuarioId
    ) {

        return ResponseEntity.ok(
                service.buscarCarrinho(id, usuarioId)
        );
    }

    @PostMapping("/{id}/itens")
    public ResponseEntity<Carrinho> adicionarItem(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long usuarioId,
            @RequestBody AdicionarItemDTO dto
    ) {

        return ResponseEntity.ok(
                service.adicionarItem(id, usuarioId, dto)
        );
    }

    @DeleteMapping("/{id}/itens/{itemId}")
    public ResponseEntity<Carrinho> removerItem(
            @PathVariable Long id,
            @PathVariable Long itemId,
            @RequestHeader("X-User-Id") Long usuarioId
    ) {

        return ResponseEntity.ok(
                service.removerItem(id, usuarioId, itemId)
        );
    }

    @DeleteMapping("/{id}/limpar")
    public ResponseEntity<Carrinho> limpar(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long usuarioId
    ) {

        return ResponseEntity.ok(
                service.limparCarrinho(id, usuarioId)
        );
    }

    @GetMapping("/order")
    public ResponseEntity<PagamentoIniciadoEvent> order(
            @RequestHeader("X-User-Id") Long usuarioId
    ) {
        return ResponseEntity.ok(
                service.buscarDadosPagamento( usuarioId)
        );
    }
}