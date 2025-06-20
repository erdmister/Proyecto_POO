para correr el proyecto debes ubicarte en la carpeta Proyecto_POO: 
```javac -cp .;lib/mysql-connector-j-9.3.0.jar src\Main.java -d src```
```java -cp "src;lib/mysql-connector-j-9.3.0.jar" src.Main```

para crear la bd: 
```
-- Crear base de datos
CREATE DATABASE sistema_inventario;
USE sistema_inventario;

-- Tabla de Productos
CREATE TABLE productos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    precio DECIMAL(10, 2) NOT NULL,
    costo DECIMAL(10, 2) NOT NULL,
    stock INT NOT NULL,
    fecha_caducidad DATE,
    UNIQUE(nombre)
);

-- Tabla de Ventas
CREATE TABLE ventas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    fecha DATETIME DEFAULT CURRENT_TIMESTAMP,
    total DECIMAL(10, 2) NOT NULL,
    metodo_pago ENUM('EFECTIVO', 'TARJETA', 'TRANSFERENCIA') NOT NULL,
    empleado_id INT,
    FOREIGN KEY (empleado_id) REFERENCES empleados(id)
);

-- Tabla de Detalles de Ventas
CREATE TABLE detalles_ventas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    venta_id INT,
    producto_id INT,
    cantidad INT NOT NULL,
    subtotal DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (venta_id) REFERENCES ventas(id),
    FOREIGN KEY (producto_id) REFERENCES productos(id)
);

-- Tabla de Empleados
CREATE TABLE empleados (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    rfc VARCHAR(13) NOT NULL UNIQUE,
    telefono VARCHAR(15),
    email VARCHAR(100),
    rol ENUM('ADMIN', 'GERENTE', 'VENDEDOR') NOT NULL,
    sucursal VARCHAR(100),
    activo BOOLEAN DEFAULT TRUE
);

-- Tabla de Proveedores
CREATE TABLE proveedores (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    ruc VARCHAR(13) NOT NULL UNIQUE,
    telefono VARCHAR(15),
    email VARCHAR(100),
    direccion VARCHAR(255),
    productos_suministrados TEXT
);

-- Tabla de Usuarios
CREATE TABLE usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    contraseña VARCHAR(255) NOT NULL,
    rol ENUM('ADMIN', 'GERENTE', 'VENDEDOR') NOT NULL,
    empleado_id INT,
    FOREIGN KEY (empleado_id) REFERENCES empleados(id)
);

-- Insertar datos de ejemplo en la tabla de productos
INSERT INTO productos (nombre, precio, costo, stock, fecha_caducidad) VALUES
('Producto A', 100.00, 70.00, 50, '2024-12-31'),
('Producto B', 200.00, 150.00, 30, '2025-01-15'),
('Producto C', 300.00, 250.00, 20, '2023-11-30');

-- Insertar datos de ejemplo en la tabla de empleados
INSERT INTO empleados (nombre, rfc, telefono, email, rol, sucursal) VALUES
('Juan Pérez', 'JUAP123456789', '555-1234', 'juan@example.com', 'ADMIN', 'Sucursal 1'),
('María López', 'MALO987654321', '555-5678', 'maria@example.com', 'VENDEDOR', 'Sucursal 2');

-- Insertar datos de ejemplo en la tabla de proveedores
INSERT INTO proveedores (nombre, ruc, telefono, email, direccion, productos_suministrados) VALUES
('Proveedor 1', 'RUC123456789', '555-8765', 'proveedor1@example.com', 'Calle Falsa 123', 'Producto A, Producto B'),
('Proveedor 2', 'RUC987654321', '555-4321', 'proveedor2@example.com', 'Avenida Siempre Viva 456', 'Producto C');

-- Insertar datos de ejemplo en la tabla de usuarios
INSERT INTO usuarios (nombre, contraseña, rol, empleado_id) VALUES
('admin', 'admin123', 'ADMIN', 1),
('vendedor', 'vendedor123', 'VENDEDOR', 2);
```