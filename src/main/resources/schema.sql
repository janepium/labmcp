CREATE DATABASE IF NOT EXISTS supermercado_mcp
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE supermercado_mcp;

CREATE TABLE IF NOT EXISTS producto (
                                        id BIGINT NOT NULL AUTO_INCREMENT,
                                        nombre VARCHAR(150) NOT NULL,
    categoria VARCHAR(100) NOT NULL,
    precio DOUBLE NOT NULL,
    stock INT NOT NULL,
    PRIMARY KEY (id)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS pedido (
                                      id BIGINT NOT NULL AUTO_INCREMENT,
                                      cliente VARCHAR(150) NOT NULL,
    direccion VARCHAR(255) NOT NULL,
    detalle_productos TEXT NOT NULL,
    total DOUBLE NOT NULL,
    estado VARCHAR(50) DEFAULT 'PENDIENTE',
    fecha_entrega VARCHAR(50) NOT NULL,
    version BIGINT DEFAULT 0,
    PRIMARY KEY (id)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Productos de ejemplo en el supermercado
INSERT INTO producto (nombre, categoria, precio, stock) VALUES
                                                            ('Leche Deslactosada 1L', 'Lácteos', 4500.0, 50),
                                                            ('Pan Integral Tajado', 'Panadería', 6200.0, 30),
                                                            ('Huevos Campesinos x12', 'Lácteos y Huevos', 11500.0, 40),
                                                            ('Arroz Blanco 5kg', 'Despensa', 22000.0, 25),
                                                            ('Aceite Vegetal 1L', 'Despensa', 12800.0, 35),
                                                            ('Café Molido 500g', 'Bebidas', 18500.0, 20),
                                                            ('Manzana Roja x1kg', 'Frutas y Verduras', 9800.0, 45);

-- Pedidos a domicilio de ejemplo
INSERT INTO pedido (cliente, direccion, detalle_productos, total, estado, fecha_entrega, version) VALUES
                                                                                                      ('Ana Gómez', 'Calle 10 # 43A-20, Medellín', '2x Leche Deslactosada 1L, 1x Pan Integral Tajado', 15200.0, 'PENDIENTE', '2026-11-15', 0),
                                                                                                      ('Carlos Ruiz', 'Carrera 70 # 52-10, Medellín', '1x Arroz Blanco 5kg, 1x Café Molido 500g', 40500.0, 'EN_CAMINO', '2026-11-14', 0);
														 