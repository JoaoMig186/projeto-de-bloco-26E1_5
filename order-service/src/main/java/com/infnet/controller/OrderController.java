package com.infnet.controller;

import com.infnet.domain.Order;
import com.infnet.dto.OrderRequestDTO;
import com.infnet.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping(" ")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> findById(@PathVariable("orderId") Long id){
        return ResponseEntity.ok(orderService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<Order>> findAll(){
        return ResponseEntity.ok(orderService.findAll());
    }

    @PostMapping("/order")
    public ResponseEntity<Order> registrerOrder(@RequestBody OrderRequestDTO request){
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.registerOrder(request));
    }

//    Delete
    @DeleteMapping("/{orderId}")
    public ResponseEntity<Order> deleteOrder(@PathVariable("orderId") Long id){
        orderService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
