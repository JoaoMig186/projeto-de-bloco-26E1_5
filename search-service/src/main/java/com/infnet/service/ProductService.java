package com.infnet.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.infnet.dtos.ProductResponseDTO;
import com.infnet.model.ProductDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ElasticsearchClient client;

    public List<ProductResponseDTO> listProductsByName(String term) throws IOException {
        SearchResponse<ProductDocument> search = client.search(s -> s
                .index("products")
                .query(q -> q
                        .match(m -> m
                                .field("name")
                                .query(term)
                        )
                ), ProductDocument.class
        );
        return search.hits().hits()
                .stream()
                .map(ProductResponseDTO::toDTO)
                .toList();
    }



}
