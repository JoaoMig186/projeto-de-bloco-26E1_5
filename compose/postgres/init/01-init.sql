CREATE SCHEMA IF NOT EXISTS user_service;
CREATE SCHEMA IF NOT EXISTS store_service;
CREATE SCHEMA IF NOT EXISTS review_service;
CREATE SCHEMA IF NOT EXISTS cart_service;
CREATE SCHEMA IF NOT EXISTS delivery_service;
CREATE SCHEMA IF NOT EXISTS orders_service;

CREATE TABLE IF NOT EXISTS user_service.tb_users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255),
    email VARCHAR(255) UNIQUE,
    password VARCHAR(255),
    role VARCHAR(50),
    status VARCHAR(50),
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS user_service.tb_customer_profiles (
    user_id BIGINT PRIMARY KEY,
    address VARCHAR(255),
    lat DOUBLE PRECISION,
    lon DOUBLE PRECISION,
    status VARCHAR(50),
    created_at TIMESTAMP,
    updated_at TIMESTAMP,

    CONSTRAINT fk_customer_profile_user
    FOREIGN KEY (user_id)
    REFERENCES user_service.tb_users(id)
);

CREATE TABLE IF NOT EXISTS store_service.tb_stores (
   id BIGSERIAL PRIMARY KEY,
   name VARCHAR(255) NOT NULL,
    cnpj VARCHAR(255) NOT NULL UNIQUE,
    address VARCHAR(255) NOT NULL,
    latitude DOUBLE PRECISION,
    longitude DOUBLE PRECISION,
    phone VARCHAR(255),
    active BOOLEAN NOT NULL DEFAULT TRUE
    );

CREATE TABLE IF NOT EXISTS store_service.tb_products (
     id BIGSERIAL PRIMARY KEY,
     name VARCHAR(255) NOT NULL,
    description VARCHAR(500),
    price NUMERIC(19,2) NOT NULL,
    stock_quantity INTEGER NOT NULL,
    category VARCHAR(255) NOT NULL,
    durability VARCHAR(255) NOT NULL,
    weight DOUBLE PRECISION NOT NULL,
    store_id BIGINT NOT NULL,

    CONSTRAINT fk_product_store
    FOREIGN KEY (store_id)
    REFERENCES store_service.tb_stores(id)
    ON DELETE CASCADE
);

