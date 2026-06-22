INSERT INTO delivery_service.drivers
(id, name, vehicle_type, available)
VALUES

-- TRUCK
(gen_random_uuid(), 'Carlos Silva', 'TRUCK', true),
(gen_random_uuid(), 'Marcos Oliveira', 'TRUCK', true),
(gen_random_uuid(), 'João Pereira', 'TRUCK', true),
(gen_random_uuid(), 'Ricardo Souza', 'TRUCK', true),
(gen_random_uuid(), 'Fernando Costa', 'TRUCK', true),

-- PICKUP
(gen_random_uuid(), 'Lucas Santos', 'PICKUP', true),
(gen_random_uuid(), 'Pedro Almeida', 'PICKUP', true),
(gen_random_uuid(), 'Gabriel Rocha', 'PICKUP', true),
(gen_random_uuid(), 'André Martins', 'PICKUP', true),
(gen_random_uuid(), 'Rafael Gomes', 'PICKUP', true),

-- MOTORCYCLE
(gen_random_uuid(), 'Bruno Lima', 'MOTORCYCLE', true),
(gen_random_uuid(), 'Thiago Ribeiro', 'MOTORCYCLE', true),
(gen_random_uuid(), 'Mateus Fernandes', 'MOTORCYCLE', true),
(gen_random_uuid(), 'Felipe Barbosa', 'MOTORCYCLE', true),
(gen_random_uuid(), 'Diego Carvalho', 'MOTORCYCLE', true);