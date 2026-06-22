INSERT INTO user_service.tb_users (id, name, email, password, status, role, created_at, updated_at)
VALUES
    (1, 'admin', 'admin@admin.com', '$2a$10$gowpBWRHV8ZVmNBKcH2SReY1Bt4GEhiWkS7Ra.DHUS99UGOweYPO.', 'ACTIVE', 'ADMIN',NOW(),NOW()),
    (2, 'customer', 'customer@customer.com', '$2a$10$gowpBWRHV8ZVmNBKcH2SReY1Bt4GEhiWkS7Ra.DHUS99UGOweYPO.', 'ACTIVE', 'CUSTOMER',NOW(),NOW()),
    (3, 'store', 'store@store.com', '$2a$10$gowpBWRHV8ZVmNBKcH2SReY1Bt4GEhiWkS7Ra.DHUS99UGOweYPO.', 'ACTIVE', 'STORE_OWNER',NOW(),NOW());

INSERT INTO user_service.tb_customer_profiles (user_id, address, lat, lon, status, created_at, updated_at)
VALUES
    (1, 'Rua das Laranjeiras, 150 - Rio de Janeiro/RJ', -22.9365, -43.1846, 'ACTIVE', NOW(), NOW()),
    (2, 'Av. Atlântica, 1702 - Copacabana/RJ', -22.9711, -43.1822, 'ACTIVE', NOW(), NOW()),
    (3, 'Rua Jardim Botânico, 1008 - Rio de Janeiro/RJ', -22.9636, -43.2230, 'PENDING_GEOCODE', NOW(), NOW());

