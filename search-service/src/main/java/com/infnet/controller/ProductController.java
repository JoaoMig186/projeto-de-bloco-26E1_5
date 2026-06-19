package com.infnet.controller;

import com.infnet.dtos.ProductResponseDTO;
import com.infnet.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/products")
@AllArgsConstructor
@Validated
public class ProductController {


    private ProductService service;

    @GetMapping("/search/name")
    public List<ProductResponseDTO> listProductsByName(@RequestParam String term) throws IOException {
        return service.listProductsByName(term);
    }

}
