package com.infnet.controller;


import com.infnet.dtos.StoreProductsDTO;
import com.infnet.service.SearchService;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/icimento")
@AllArgsConstructor
@Validated
public class SearchController {


    private SearchService searchService;

    @GetMapping("/search")
    public List<StoreProductsDTO> listProductsByName(@RequestParam String term) throws IOException {
        return searchService.listProductsByMultimatchFuzzyGroupedByStore(term);
    }

}
