
CREATE DATABASE IF NOT EXISTS inventa_db;
USE inventa_db;

-- Tabla de Clientes
CREATE TABLE IF NOT EXISTS clientes (
    id_cliente    INT AUTO_INCREMENT PRIMARY KEY,
    nombre        VARCHAR(100) NOT NULL,
    apellido      VARCHAR(100) NOT NULL,
    documento     VARCHAR(20)  NOT NULL UNIQUE,
    correo        VARCHAR(150),
    telefono      VARCHAR(20),
    direccion     VARCHAR(200),
    fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de Productos
CREATE TABLE IF NOT EXISTS productos (
    id_producto   INT AUTO_INCREMENT PRIMARY KEY,
    nombre        VARCHAR(150) NOT NULL,
    descripcion   VARCHAR(255),
    precio        DECIMAL(10,2) NOT NULL,
    stock         INT NOT NULL DEFAULT 0,
    categoria     VARCHAR(80),
    fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de Ventas (encabezado)
CREATE TABLE IF NOT EXISTS ventas (
    id_venta      INT AUTO_INCREMENT PRIMARY KEY,
    id_cliente    INT NOT NULL,
    fecha_venta   DATETIME DEFAULT CURRENT_TIMESTAMP,
    total         DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    estado        VARCHAR(30) DEFAULT 'ACTIVA',
    FOREIGN KEY (id_cliente) REFERENCES clientes(id_cliente)
);

-- Tabla de Detalles de Venta
CREATE TABLE IF NOT EXISTS detalle_venta (
    id_detalle    INT AUTO_INCREMENT PRIMARY KEY,
    id_venta      INT NOT NULL,
    id_producto   INT NOT NULL,
    cantidad      INT NOT NULL,
    precio_unitario DECIMAL(10,2) NOT NULL,
    subtotal      DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (id_venta)    REFERENCES ventas(id_venta),
    FOREIGN KEY (id_producto) REFERENCES productos(id_producto)
);

-- Datos de prueba - Clientes
INSERT INTO clientes (nombre, apellido, documento, correo, telefono, direccion) VALUES
('Carlos', 'Ramírez', '1020304050', 'carlos.ramirez@email.com', '3101234567', 'Calle 10 # 5-20'),
('Laura',  'Gómez',   '1098765432', 'laura.gomez@email.com',   '3209876543', 'Carrera 8 # 15-30'),
('Pedro',  'Torres',  '1045678901', 'pedro.torres@email.com',  '3156789012', 'Avenida 6 # 22-10');

-- Datos de prueba - Productos
INSERT INTO productos (nombre, descripcion, precio, stock, categoria) VALUES
('Camisa Polo',     'Camisa polo talla M color azul', 45000.00, 50, 'Ropa'),
('Pantalón Jean',   'Jean clásico talla 32',          89000.00, 30, 'Ropa'),
('Zapatos Cuero',   'Zapatos de cuero café talla 42', 150000.00, 20, 'Calzado'),
('Bolso Dama',      'Bolso de mano color negro',       75000.00, 15, 'Accesorios'),
('Correa Cuero',    'Correa de cuero negra talla L',   35000.00, 40, 'Accesorios');
