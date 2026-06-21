package com.infnet.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import co.elastic.clients.elasticsearch._types.aggregations.StringTermsAggregate;
import co.elastic.clients.elasticsearch._types.aggregations.StringTermsBucket;
import co.elastic.clients.elasticsearch._types.aggregations.TopHitsAggregate;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.infnet.dtos.ProductResponseDTO;
import com.infnet.dtos.StoreProductsDTO;
import com.infnet.kafka.KafkaSearchListener;
import com.infnet.model.ProductDocument;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final ElasticsearchClient client;
    private static final Logger log = LoggerFactory.getLogger(SearchService.class);
    public List<StoreProductsDTO> listProductsByMultimatchFuzzyGroupedByStore(String term) throws IOException {

        log.info("Termo pesquisado: {}", term);

        SearchResponse<ProductDocument> search = client.search(
                s -> s
                        .index("products")
                        .query(q -> q
                                .multiMatch(m -> m
                                        .query(term)
                                        .fields("name", "description", "category")
                                        .fuzziness("AUTO")
                                )
                        ),
                ProductDocument.class
        );

        log.info("Total de hits: {}", search.hits().total());

        search.hits().hits().forEach(hit ->
                log.info("Documento encontrado: {}", hit.source())
        );

        List<ProductResponseDTO> hits = search.hits().hits()
                .stream()
                .map(ProductResponseDTO::toDTO)
                .toList();

        log.info("DTOs gerados: {}", hits.size());

        List<StoreProductsDTO> storeProducts = new ArrayList<>();

        for (ProductResponseDTO product : hits) {

            log.info("Processando produto: {}", product);

            Long storeId = product.storeId();

            StoreProductsDTO store = storeProducts.stream()
                    .filter(s -> s.storeId().equals(storeId))
                    .findFirst()
                    .orElse(null);

            if (store == null) {
                log.info("Criando grupo para loja {}", storeId);

                store = new StoreProductsDTO(
                        storeId,
                        product.storeName(),
                        new ArrayList<>()
                );

                storeProducts.add(store);
            }

            store.products().add(product);
        }

        log.info("Resultado final: {}", storeProducts);

        return storeProducts;
    }
}
