--> USUARIOS
INSERT INTO user_service.tb_users (id, name, email, password, status, role, created_at, updated_at)
VALUES
    (1, 'admin', 'admin@admin.com', '$2a$10$gowpBWRHV8ZVmNBKcH2SReY1Bt4GEhiWkS7Ra.DHUS99UGOweYPO.', 'ACTIVE', 'ADMIN',NOW(),NOW()),
    (2, 'customer', 'customer@customer.com', '$2a$10$gowpBWRHV8ZVmNBKcH2SReY1Bt4GEhiWkS7Ra.DHUS99UGOweYPO.', 'ACTIVE', 'CUSTOMER',NOW(),NOW()),
    (3, 'store', 'store@store.com', '$2a$10$gowpBWRHV8ZVmNBKcH2SReY1Bt4GEhiWkS7Ra.DHUS99UGOweYPO.', 'ACTIVE', 'STORE_OWNER',NOW(),NOW());

--> CLIENTES
INSERT INTO user_service.tb_customer_profiles (user_id, address, lat, lon, status, created_at, updated_at)
VALUES
    (1, 'Rua das Laranjeiras, 150 - Rio de Janeiro/RJ', -22.9365, -43.1846, 'ACTIVE', NOW(), NOW()),
    (2, 'Av. Atlântica, 1702 - Copacabana/RJ', -22.9711, -43.1822, 'ACTIVE', NOW(), NOW()),
    (3, 'Rua Jardim Botânico, 1008 - Rio de Janeiro/RJ', -22.9636, -43.2230, 'PENDING_GEOCODE', NOW(), NOW());

--> LOJAS
INSERT INTO store_service.tb_stores (owner_id, name, cnpj, address, latitude, longitude, phone, active, average_rating, total_reviews)
VALUES
    (1, 'Constrular', '12345678000101', 'Rua A, 100', -22.90, -43.20, '21999990001', TRUE, 0, 0),
    (1, 'Casa do Pedreiro', '12345678000102', 'Rua B, 200', -22.91, -43.21, '21999990002', TRUE, 0, 0),
    (1, 'Mundo da Construção', '12345678000103', 'Rua C, 300', -22.92, -43.22, '21999990003', TRUE, 0, 0);

--> PRODUTOS
INSERT INTO store_service.tb_products
(name, description, price, stock_quantity, category, durability, weight, store_id)
VALUES
    ('Cimento CP-II 50kg', 'Saco de cimento para obras', 42.90, 500, 'CIMENTOS', 'NAO_FRAGIL', 50.0, 1),

    ('Porcelanato Bege 90x90', 'Piso porcelanato premium', 89.90, 300, 'PISOS', 'FRAGIL', 18.0, 1),

    ('Tinta Acrílica Branca 18L', 'Tinta para áreas internas', 219.90, 100, 'TINTAS', 'NAO_FRAGIL', 18.0, 1),

    ('Martelo Profissional', 'Martelo de aço com cabo emborrachado', 45.90, 80, 'FERRAMENTAS', 'NAO_FRAGIL', 1.2, 1),

    ('Tijolo Cerâmico', 'Tijolo para alvenaria', 1.20, 5000, 'TIJOLOS', 'FRAGIL', 2.5, 2),

    ('Areia Média 20kg', 'Areia para construção', 12.90, 1000, 'AREIA', 'NAO_FRAGIL', 20.0, 2),

    ('Brita 1 20kg', 'Brita para concreto', 15.90, 800, 'BRITA', 'NAO_FRAGIL', 20.0, 2),

    ('Argamassa AC3 20kg', 'Argamassa para porcelanato', 34.90, 250, 'ARGAMASSAS', 'NAO_FRAGIL', 20.0, 2),

    ('Tubo PVC 100mm', 'Tubo para esgoto', 59.90, 120, 'HIDRAULICA', 'NAO_FRAGIL', 2.0, 3),

    ('Disjuntor 20A', 'Disjuntor monopolar', 18.90, 150, 'ELETRICA', 'NAO_FRAGIL', 0.2, 3),

    ('Lâmpada LED 15W', 'Lâmpada econômica', 12.90, 300, 'ILUMINACAO', 'FRAGIL', 0.1, 3),

    ('Porta de Madeira', 'Porta interna de madeira maciça', 399.90, 30, 'PORTAS', 'NAO_FRAGIL', 25.0, 3);

--> OUTBOX
INSERT INTO store_service.tb_product_events
(id, product_id, event_type, payload, processed)
VALUES
    (gen_random_uuid(), 1, 'PRODUCT_CREATED',
     '{"id":1,"name":"Cimento CP-II 50kg","description":"Saco de cimento para obras","category":"CIMENTOS","durability":"NAO_FRAGIL","price":42.90,"storeId":1,"storeName":"Constrular"}'::jsonb,
     false),

    (gen_random_uuid(), 2, 'PRODUCT_CREATED',
     '{"id":2,"name":"Porcelanato Bege 90x90","description":"Piso porcelanato premium","category":"PISOS","durability":"FRAGIL","price":89.90,"storeId":1,"storeName":"Constrular"}'::jsonb,
     false),

    (gen_random_uuid(), 3, 'PRODUCT_CREATED',
     '{"id":3,"name":"Tinta Acrílica Branca 18L","description":"Tinta para áreas internas","category":"TINTAS","durability":"NAO_FRAGIL","price":219.90,"storeId":1,"storeName":"Constrular"}'::jsonb,
     false),

    (gen_random_uuid(), 4, 'PRODUCT_CREATED',
     '{"id":4,"name":"Martelo Profissional","description":"Martelo de aço com cabo emborrachado","category":"FERRAMENTAS","durability":"NAO_FRAGIL","price":45.90,"storeId":1,"storeName":"Constrular"}'::jsonb,
     false),

    (gen_random_uuid(), 5, 'PRODUCT_CREATED',
     '{"id":5,"name":"Tijolo Cerâmico","description":"Tijolo para alvenaria","category":"TIJOLOS","durability":"FRAGIL","price":1.20,"storeId":2,"storeName":"Casa do Pedreiro"}'::jsonb,
     false),

    (gen_random_uuid(), 6, 'PRODUCT_CREATED',
     '{"id":6,"name":"Areia Média 20kg","description":"Areia para construção","category":"AREIA","durability":"NAO_FRAGIL","price":12.90,"storeId":2,"storeName":"Casa do Pedreiro"}'::jsonb,
     false),

    (gen_random_uuid(), 7, 'PRODUCT_CREATED',
     '{"id":7,"name":"Brita 1 20kg","description":"Brita para concreto","category":"BRITA","durability":"NAO_FRAGIL","price":15.90,"storeId":2,"storeName":"Casa do Pedreiro"}'::jsonb,
     false),

    (gen_random_uuid(), 8, 'PRODUCT_CREATED',
     '{"id":8,"name":"Argamassa AC3 20kg","description":"Argamassa para porcelanato","category":"ARGAMASSAS","durability":"NAO_FRAGIL","price":34.90,"storeId":2,"storeName":"Casa do Pedreiro"}'::jsonb,
     false),

    (gen_random_uuid(), 9, 'PRODUCT_CREATED',
     '{"id":9,"name":"Tubo PVC 100mm","description":"Tubo para esgoto","category":"HIDRAULICA","durability":"NAO_FRAGIL","price":59.90,"storeId":3,"storeName":"Mundo da Construção"}'::jsonb,
     false),

    (gen_random_uuid(), 10, 'PRODUCT_CREATED',
     '{"id":10,"name":"Disjuntor 20A","description":"Disjuntor monopolar","category":"ELETRICA","durability":"NAO_FRAGIL","price":18.90,"storeId":3,"storeName":"Mundo da Construção"}'::jsonb,
     false),

    (gen_random_uuid(), 11, 'PRODUCT_CREATED',
     '{"id":11,"name":"Lâmpada LED 15W","description":"Lâmpada econômica","category":"ILUMINACAO","durability":"FRAGIL","price":12.90,"storeId":3,"storeName":"Mundo da Construção"}'::jsonb,
     false),

    (gen_random_uuid(), 12, 'PRODUCT_CREATED',
     '{"id":12,"name":"Porta de Madeira","description":"Porta interna de madeira maciça","category":"PORTAS","durability":"NAO_FRAGIL","price":399.90,"storeId":3,"storeName":"Mundo da Construção"}'::jsonb,
     false);