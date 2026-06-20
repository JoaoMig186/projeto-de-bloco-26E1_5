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

    public List<ProductResponseDTO> listProductsByFuzzName(String term) throws IOException {
        SearchResponse<ProductDocument> search = client.search(s -> s
                .index("products")
                .query(q -> q
                        .fuzzy(f -> f
                                .field("name")
                                .value(term)
                                .fuzziness("AUTO")
                        )
                ), ProductDocument.class
        );
        return search.hits().hits()
                .stream()
                .map(ProductResponseDTO::toDTO)
                .toList();
    }

    public List<ProductResponseDTO> listProductsByMultiFields(String term) throws IOException {
        SearchResponse<ProductDocument> search = client.search(s -> s
                .index("products")
                .query(q -> q
                        .multiMatch(m -> m
                                .fields("name", "description")
                                .query(term)
                        )
                ), ProductDocument.class
        );

        return search.hits().hits()
                .stream()
                .map(ProductResponseDTO::toDTO)
                .toList();
    }

    public List<ProductResponseDTO> listProductsByPriceRange(Double min, Double max) throws IOException {

        SearchResponse<ProductDocument> search = client.search(s -> s
                .index("products")
                .query(q -> q
                        .range(r -> r
                                .number(n -> n
                                        .field("price")
                                        .gte(min)
                                        .lte(max)
                                )
                        )
                ), ProductDocument.class
        );

        return search.hits().hits()
                .stream()
                .map(ProductResponseDTO::toDTO)
                .toList();
    }




}
