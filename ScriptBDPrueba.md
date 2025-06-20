```
-- Creación de la base de datos
CREATE DATABASE IF NOT EXISTS sistema_inventario;
USE sistema_inventario;

-- Tabla de usuarios
CREATE TABLE usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre_usuario VARCHAR(50) NOT NULL UNIQUE,
    contraseña VARCHAR(255) NOT NULL,
    rol ENUM('ADMIN', 'GERENTE', 'CAJERO') NOT NULL,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    activo BOOLEAN DEFAULT TRUE
);

-- Tabla de productos
CREATE TABLE productos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    precio DECIMAL(10, 2) NOT NULL,
    costo DECIMAL(10, 2) NOT NULL,
    stock INT NOT NULL DEFAULT 0,
    stock_minimo INT DEFAULT 5,
    fecha_caducidad DATE,
    codigo_barras VARCHAR(50) UNIQUE,
    categoria VARCHAR(50),
    proveedor_id INT,
    creado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    actualizado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CHECK (precio >= 0),
    CHECK (costo >= 0),
    CHECK (stock >= 0)
);

-- Tabla de empleados
CREATE TABLE empleados (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    rfc VARCHAR(13) UNIQUE,
    curp VARCHAR(18) UNIQUE,
    telefono VARCHAR(15),
    email VARCHAR(100) UNIQUE,
    direccion TEXT,
    rol VARCHAR(50),
    sucursal VARCHAR(50),
    fecha_contratacion DATE,
    salario DECIMAL(10, 2),
    usuario_id INT UNIQUE,
    activo BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);

-- Tabla de proveedores
CREATE TABLE proveedores (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    ruc VARCHAR(20) UNIQUE,
    telefono VARCHAR(15),
    email VARCHAR(100),
    direccion TEXT,
    productos_suministrados TEXT,
    contacto_principal VARCHAR(100),
    dias_credito INT DEFAULT 0,
    activo BOOLEAN DEFAULT TRUE
);

-- Tabla de ventas
CREATE TABLE ventas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    empleado_id INT NOT NULL,
    cliente_id INT,
    total DECIMAL(12, 2) NOT NULL,
    efectivo_recibido DECIMAL(12, 2),
    cambio DECIMAL(12, 2),
    metodo_pago ENUM('EFECTIVO', 'TARJETA', 'TRANSFERENCIA') NOT NULL,
    estado ENUM('PENDIENTE', 'COMPLETADA', 'CANCELADA') DEFAULT 'COMPLETADA',
    FOREIGN KEY (empleado_id) REFERENCES empleados(id)
);

-- Tabla de items de venta
CREATE TABLE items_venta (
    id INT AUTO_INCREMENT PRIMARY KEY,
    venta_id INT NOT NULL,
    producto_id INT NOT NULL,
    cantidad INT NOT NULL,
    precio_unitario DECIMAL(10, 2) NOT NULL,
    subtotal DECIMAL(12, 2) NOT NULL,
    FOREIGN KEY (venta_id) REFERENCES ventas(id) ON DELETE CASCADE,
    FOREIGN KEY (producto_id) REFERENCES productos(id),
    CHECK (cantidad > 0)
);

-- Tabla de clientes (opcional)
CREATE TABLE clientes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    rfc VARCHAR(13),
    telefono VARCHAR(15),
    email VARCHAR(100),
    direccion TEXT,
    puntos_acumulados INT DEFAULT 0
);

-- Actualizar la tabla ventas para incluir cliente_id como FK
ALTER TABLE ventas
ADD CONSTRAINT fk_venta_cliente
FOREIGN KEY (cliente_id) REFERENCES clientes(id);

-- Tabla de inventario (historial de movimientos)
CREATE TABLE movimientos_inventario (
    id INT AUTO_INCREMENT PRIMARY KEY,
    producto_id INT NOT NULL,
    tipo ENUM('ENTRADA', 'SALIDA', 'AJUSTE') NOT NULL,
    cantidad INT NOT NULL,
    motivo VARCHAR(100),
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    usuario_id INT NOT NULL,
    referencia_id INT,
    FOREIGN KEY (producto_id) REFERENCES productos(id),
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);

-- Índices para mejorar el rendimiento
CREATE INDEX idx_producto_nombre ON productos(nombre);
CREATE INDEX idx_producto_categoria ON productos(categoria);
CREATE INDEX idx_venta_fecha ON ventas(fecha);
CREATE INDEX idx_venta_empleado ON ventas(empleado_id);
CREATE INDEX idx_movimiento_producto ON movimientos_inventario(producto_id);

-- Datos iniciales (usuarios de ejemplo)
-- Contraseña: admin123 (se debe hashear en una implementación real)
INSERT INTO usuarios (nombre_usuario, contraseña, rol) VALUES
('admin', '$2a$10$HxQZZnlWY.z8GQGZXkI2YO5GQzUhwRHyzUHGZpkZJXVR5C2eBojQO', 'ADMIN'),
('gerente1', '$2a$10$HxQZZnlWY.z8GQGZXkI2YO5GQzUhwRHyzUHGZpkZJXVR5C2eBojQO', 'GERENTE'),
('cajero1', '$2a$10$HxQZZnlWY.z8GQGZXkI2YO5GQzUhwRHyzUHGZpkZJXVR5C2eBojQO', 'CAJERO');

-- Datos iniciales para empleados
INSERT INTO empleados (nombre, rfc, telefono, email, rol, sucursal, usuario_id) VALUES
('Administrador Sistema', 'XAXX010101000', '5551234567', 'admin@empresa.com', 'Administrador', 'Matriz', 1),
('Gerente General', 'XEXX010101000', '5557654321', 'gerente@empresa.com', 'Gerente', 'Matriz', 2);

-- Datos iniciales para productos
INSERT INTO productos (nombre, precio, costo, stock, fecha_caducidad) VALUES
('Lápiz HB', 5.50, 3.20, 100, '2025-12-31'),
('Cuaderno Profesional', 45.90, 28.50, 50, '2026-12-31'),
('Borrador Blanco', 3.75, 1.80, 200, '2028-12-31');

-- Creación de usuario para la aplicación (permisos restringidos)
CREATE USER 'app_inventario'@'localhost' IDENTIFIED BY 'PasswordSeguro123';
GRANT SELECT, INSERT, UPDATE, DELETE ON sistema_inventario.* TO 'app_inventario'@'localhost';
FLUSH PRIVILEGES;

```