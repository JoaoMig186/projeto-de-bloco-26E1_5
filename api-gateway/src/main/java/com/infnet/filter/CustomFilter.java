package com.infnet.filter;

import com.infnet.service.JwtService;
import io.jsonwebtoken.Claims;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.function.HandlerFilterFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import java.util.Map;

@Configuration
public class CustomFilter {
    private final JwtService jwtService;

    public CustomFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Bean
    public HandlerFilterFunction<ServerResponse,ServerResponse> authFilter(){
        return (request, next) -> {
            String authorization = request.headers().firstHeader("Authorization");

            if(authorization == null || authorization.isBlank()){
                return ServerResponse.status(HttpStatus.BAD_REQUEST).body(Map.of("message","Forbidden authorization"));
            }
            String token = authorization.split(" ")[1];
            Claims claims = jwtService.validateToken(token);
            String userId = String.valueOf(claims.get("id"));
            ServerRequest roleRequest = ServerRequest.from(request)
                    .header("X-User-Role", claims.get("role", String.class))
                    .header("X-User-Name", claims.get("name", String.class))
                    .header("X-User-Id", userId)
                    .build();

            return next.handle(roleRequest);
        };
    }

}

