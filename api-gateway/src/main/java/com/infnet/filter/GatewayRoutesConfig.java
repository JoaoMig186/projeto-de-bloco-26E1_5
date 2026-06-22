package com.infnet.filter;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.uri;
import static org.springframework.cloud.gateway.server.mvc.filter.LoadBalancerFilterFunctions.lb;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;
import static org.springframework.cloud.gateway.server.mvc.predicate.GatewayRequestPredicates.method;
import static org.springframework.cloud.gateway.server.mvc.predicate.GatewayRequestPredicates.path;

@Configuration
public class GatewayRoutesConfig {

    @Bean
    public RouterFunction<ServerResponse> gatewayRoutes(CustomFilter filter) {

        return route("authenticated-store")
                .route(path("/stores/**"), http())
                .filter(lb("STORE-SERVICE"))
                .filter(filter.authFilter())
                .build()
                .and(route("authenticated-review")
                    .route(path("/reviews/**").and(method(HttpMethod.POST)), http())
                    .filter(lb("REVIEW-SERVICE"))
                    .filter(filter.authFilter())
                    .build()
                .and(route("authenticated-cart")
                    .route(path("/carts/**"), http())
                    .filter(lb("CART-SERVICE"))
                    .filter(filter.authFilter())
                    .build()
                .and(route("authenticated-delivery")
                    .route(path("/deliveries/**"), http())
                    .filter(lb("DELIVERY-SERVICE"))
                    .filter(filter.authFilter())
                    .build()
                .and(route("authenticated-drivers")
                    .route(path("/drivers/**"), http())
                    .filter(lb("DELIVERY-SERVICE"))
                    .filter(filter.authFilter())
                    .build()
                .and(route("authenticated-freight")
                    .route(path("/freight/**"), http())
                    .filter(lb("DELIVERY-SERVICE"))
                    .filter(filter.authFilter())
                    .build()
                .and(route("authenticated-order")
                    .route(path("/orders/**"), http())
                    .filter(lb("ORDER-SERVICE"))
                    .filter(filter.authFilter())
                    .build()))))));

    }
}