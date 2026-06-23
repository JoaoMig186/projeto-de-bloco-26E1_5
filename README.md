# iCimento — Plataforma de Marketplace em Microsserviços

Aplicação de marketplace construída com arquitetura de microsserviços usando **Spring Boot** e **Spring Cloud**. O sistema permite que clientes pesquisem produtos, montem carrinhos, realizem pedidos e acompanhem entregas, enquanto lojistas gerenciam suas lojas e produtos.

---

## Arquitetura

```
                        ┌─────────────┐
                        │  API Gateway │  :9999
                        │ (JWT Filter) │
                        └──────┬──────┘
                               │
              ┌────────────────┼────────────────────┐
              │                │                    │
     ┌────────▼──────┐ ┌───────▼──────┐ ┌──────────▼──────┐
     │  user-service │ │ store-service│ │  search-service │
     │    :8081      │ │              │ │                 │
     └───────────────┘ └──────────────┘ └─────────────────┘
              │                │                    │
     ┌────────▼──────┐ ┌───────▼──────┐ ┌──────────▼──────┐
     │  cart-service │ │ order-service│ │delivery-service │
     │    :8086      │ │    :8091     │ │     :8082       │
     └───────────────┘ └──────────────┘ └─────────────────┘
              │                │                    │
              └────────────────▼────────────────────┘
                           Apache Kafka
              ┌────────────────┼────────────────────┐
              │                │                    │
     ┌────────▼──────┐ ┌───────▼──────┐ ┌──────────▼──────┐
     │ review-service│ │geocode-fake  │ │ discovery-server│
     │               │ │   service    │ │     :8761       │
     └───────────────┘ └──────────────┘ └─────────────────┘
```

---

## Microsserviços

| Serviço | Porta | Banco | Descrição |
|---|---|---|---|
| `api-gateway` | 9999 | — | Roteamento e validação de JWT |
| `discovery-server` | 8761 | — | Eureka Server (registro de serviços) |
| `user-service` | 8081 | PostgreSQL | Cadastro, autenticação e perfil de usuários |
| `store-service` | — | PostgreSQL | Gerenciamento de lojas e produtos |
| `cart-service` | 8086 | PostgreSQL | Carrinho de compras |
| `order-service` | 8091 | PostgreSQL | Criação e gestão de pedidos |
| `delivery-service` | 8082 | PostgreSQL | Entregas e cálculo de frete |
| `search-service` | — | Elasticsearch | Busca fuzzy de produtos |
| `review-service` | — | PostgreSQL | Avaliações de lojas |
| `geocode-fake-service` | — | Memória | Geocodificação via eventos Kafka |

---

## Fluxo Principal

```
1. Usuário se cadastra → user-service emite evento Kafka → geocode-fake-service atribui coordenadas

2. Lojista cria loja/produto → store-service emite evento Kafka → geocode-fake-service / search-service sincronizam

3. Cliente pesquisa produto → GET /icimento/search?term=xxx → search-service (Elasticsearch com busca fuzzy)

4. Cliente monta carrinho → POST /carts / POST /carts/items → cart-service valida produto no store-service

5. Cliente finaliza pedido → POST /orders/order → order-service:
   - Busca carrinho no cart-service
   - Calcula distância (Haversine) entre loja e cliente
   - Calcula frete no delivery-service (Strategy Pattern por tipo de veículo)
   - Persiste pedido e emite evento Kafka "payment-approvated-topic"

6. delivery-service consome o evento → cria entrega automaticamente

7. Entregador atualiza status → delivery-service emite evento → order-service atualiza status do pedido
```

---

## Tópicos Kafka

| Tópico | Produtor | Consumidor | Descrição |
|---|---|---|---|
| `icimento.customer.created` | user-service | geocode-fake-service | Novo cliente criado |
| `icimento.store.created` | store-service | geocode-fake-service | Nova loja criada |
| `icimento.user.geocode` | geocode-fake-service | user-service | Coordenadas do cliente |
| `icimento.store.geocode` | geocode-fake-service | store-service | Coordenadas da loja |
| `product-sync-topic` | store-service | search-service | Sincronização de produtos |
| `payment-approvated-topic` | order-service | delivery-service | Pagamento aprovado |
| `delivery-updated-topic` | delivery-service | order-service | Status de entrega atualizado |

---

## Padrões e Tecnologias

- **Service Discovery**: Eureka (Spring Cloud Netflix)
- **API Gateway**: Spring Cloud Gateway com filtro JWT (RS256)
- **Resiliência**: Resilience4j — Circuit Breaker, Retry e Rate Limiter
- **Mensageria**: Apache Kafka 4.1 (KRaft mode, sem Zookeeper)
- **Busca**: Elasticsearch com busca multimatch fuzzy
- **Observabilidade**: Prometheus + Grafana (dashboards por serviço)
- **Rastreamento Distribuído**: Zipkin
- **Logs Centralizados**: Stack ELK (Elasticsearch + Logstash + Kibana)
- **Padrão Outbox**: store-service para garantir entrega de eventos de produto
- **Strategy Pattern**: cálculo de frete por tipo de veículo (delivery-service)
- **Roles de usuário**: `CUSTOMER`, `STORE_OWNER`, `ADMIN`

---

## Identificação do Projeto

- **GroupId:** `com.infnet`
- **Java:** 21 (Amazon Corretto 21.0.11)
- **Spring Boot:** 4.0.6
- **Build:** Maven

---

## Pré-requisitos

- Docker e Docker Compose
- Java 21+
- Maven 3.9+

---

## Como Executar

### 1. Subir toda a infraestrutura e serviços

```bash
cd compose
docker-compose up -d --build
```

### 2. (Opcional) Subir stack ELK separadamente

```bash
cd compose
docker-compose -f docker-compose-elk.yml up -d
```

### 3. Verificar saúde dos serviços

Acesse o Eureka Dashboard: [http://localhost:8761](http://localhost:8761)

---

## Portas dos Serviços de Infraestrutura

| Serviço | Porta | URL |
|---|---|---|
| Eureka (Discovery) | 8761 | http://localhost:8761 |
| API Gateway | 9999 | http://localhost:9999 |
| PostgreSQL | 5432 | — |
| Kafka | 9092 | — |
| Kafka UI | 8085 | http://localhost:8085 |
| Elasticsearch | 9201 | http://localhost:9201 |
| Prometheus | 9090 | http://localhost:9090 |
| Grafana | 3000 | http://localhost:3000 (admin/admin) |
| Zipkin | 9411 | http://localhost:9411 |
| ELK Elasticsearch | 9200 | http://localhost:9200 |
| Kibana | 5601 | http://localhost:5601 |

---

## API — Principais Endpoints

### Usuários (`/users`)

| Método | Rota | Descrição |
|---|---|---|
| `POST` | `/users/login` | Autenticação (retorna JWT) |
| `POST` | `/users/customer` | Cadastro de cliente |
| `POST` | `/users/store` | Cadastro de lojista |
| `POST` | `/users/admin` | Cadastro de administrador |
| `GET` | `/users/{userId}` | Buscar usuário por ID |
| `GET` | `/users` | Listar todos os usuários (ADMIN) |
| `PUT` | `/users/activate/{userId}` | Ativar usuário |
| `PUT` | `/users/deactivate/{userId}` | Desativar usuário |

### Lojas e Produtos (`/stores`)

| Método | Rota | Descrição |
|---|---|---|
| `POST` | `/stores` | Criar loja |
| `GET` | `/stores` | Listar lojas ativas |
| `GET` | `/stores/{id}` | Buscar loja por ID |
| `DELETE` | `/stores/{id}` | Desativar loja |
| `POST` | `/stores/products` | Criar produto |
| `GET` | `/stores/{storeId}/products` | Produtos de uma loja |

### Busca (`/icimento`)

| Método | Rota | Descrição |
|---|---|---|
| `GET` | `/icimento/search?term={termo}` | Busca fuzzy de produtos (Elasticsearch) |

### Carrinho (`/carts`)

| Método | Rota | Descrição |
|---|---|---|
| `POST` | `/carts` | Criar carrinho |
| `GET` | `/carts` | Obter carrinho ativo |
| `POST` | `/carts/items` | Adicionar item ao carrinho |
| `DELETE` | `/carts/items/{itemId}` | Remover item |
| `DELETE` | `/carts/clear` | Limpar carrinho |
| `POST` | `/carts/finalize` | Finalizar carrinho |
| `GET` | `/carts/order` | Obter dados para pagamento |

### Pedidos (`/orders`)

| Método | Rota | Descrição |
|---|---|---|
| `POST` | `/orders/order` | Criar pedido |
| `GET` | `/orders` | Listar todos os pedidos |
| `GET` | `/orders/{orderId}` | Buscar pedido por ID |
| `DELETE` | `/orders/{orderId}` | Deletar pedido |

### Entregas (`/deliveries`)

| Método | Rota | Descrição |
|---|---|---|
| `POST` | `/deliveries` | Criar entrega |
| `GET` | `/deliveries` | Listar entregas (paginado) |
| `GET` | `/deliveries/{id}` | Buscar entrega por ID |
| `PATCH` | `/deliveries/{id}/start` | Iniciar entrega |
| `PATCH` | `/deliveries/{id}/finish` | Finalizar entrega |
| `PATCH` | `/deliveries/{id}/cancel` | Cancelar entrega |

### Avaliações (`/reviews`)

| Método | Rota | Descrição |
|---|---|---|
| `POST` | `/reviews/store/{storeId}` | Criar avaliação |
| `POST` | `/reviews/{reviewId}/reply` | Responder avaliação |
| `GET` | `/reviews/store/{storeId}` | Listar avaliações de uma loja |
| `GET` | `/reviews/store/{storeId}/summary` | Resumo/média de avaliações |

---

## Autenticação

O gateway valida o JWT (RS256) em todas as rotas protegidas e injeta os headers:

- `X-User-Id` — ID do usuário autenticado
- `X-User-Role` — Role do usuário (`CUSTOMER`, `STORE_OWNER`, `ADMIN`)
- `X-User-Name` — Nome do usuário

Rotas públicas (sem JWT): `/users/login`, `/users/customer`, `/users/store`, `/reviews/**` (GET), `/icimento/**`

---

## Dashboards Grafana

Os dashboards JSON estão em `dashboard/` e podem ser importados no Grafana:

| Dashboard | Arquivo |
|---|---|
| Cart — Métricas de negócio | `icimento-cart-business-dashboard.json` |
| Delivery — Métricas de negócio | `icimento-delivery-service-dashboard.json` |
| Order — Métricas de negócio | `icimento-order-service-dashboard.json` |
| Order — JVM | `icimento-order-service-jvm-dashboard.json` |
| User — Métricas de negócio | `icimento-user-business-dashboard.json` |
| User — JVM | `icimento-user-jvm-dashboard.json` |

---

## Estrutura do Repositório

```
projeto-de-bloco-26E1_5/
├── api-gateway/
├── discovery-server/
├── user-service/
├── store-service/
├── cart-service/
├── order-service/
├── delivery-service/
├── search-service/
├── review-service/
├── geocode-fake-service/
├── compose/
│   ├── docker-compose.yml        # Stack principal
│   ├── docker-compose-elk.yml    # Stack ELK (opcional)
│   ├── prometheus/
│   │   └── prometheus.yml
│   ├── postgres/
│   │   └── init/                 # Scripts SQL de inicialização
│   └── logstash/
│       └── pipeline/
└── dashboard/                    # Dashboards Grafana (JSON)
```