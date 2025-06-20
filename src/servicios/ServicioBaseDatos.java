package src.servicios;

import java.sql.*;

public class ServicioBaseDatos {

    public static void inicializarBaseDatos(String usuario, String contrasenia) throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");

        String urlSinBD = "jdbc:mysql://localhost:3306/?useSSL=false&serverTimezone=UTC";
        String urlConBD = "jdbc:mysql://localhost:3306/sistema_inventario?useSSL=false&serverTimezone=UTC";

        // Crear la base de datos si no existe
        try (Connection conexion = DriverManager.getConnection(urlSinBD, usuario, contrasenia);
             Statement stmt = conexion.createStatement()) {
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS sistema_inventario");
        }

        // Crear tablas e insertar datos mínimos
        try (Connection conexion = DriverManager.getConnection(urlConBD, usuario, contrasenia);
             Statement stmt = conexion.createStatement()) {

            crearTablas(stmt);
            insertarDatosIniciales(stmt);
        }
    }

    private static void crearTablas(Statement stmt) throws SQLException {
        String[] sentenciasSQL = {
                "CREATE TABLE IF NOT EXISTS productos (" +
                        " id INT AUTO_INCREMENT PRIMARY KEY," +
                        " nombre VARCHAR(100) NOT NULL," +
                        " precio DECIMAL(10, 2) NOT NULL," +
                        " costo DECIMAL(10, 2) NOT NULL," +
                        " stock INT NOT NULL," +
                        " fecha_caducidad DATE," +
                        " UNIQUE(nombre))",

                "CREATE TABLE IF NOT EXISTS empleados (" +
                        " id INT AUTO_INCREMENT PRIMARY KEY," +
                        " nombre VARCHAR(100) NOT NULL," +
                        " rfc VARCHAR(13) NOT NULL UNIQUE," +
                        " telefono VARCHAR(15)," +
                        " email VARCHAR(100)," +
                        " rol ENUM('ADMIN', 'GERENTE', 'VENDEDOR') NOT NULL," +
                        " sucursal VARCHAR(100)," +
                        " activo BOOLEAN DEFAULT TRUE)",

                "CREATE TABLE IF NOT EXISTS ventas (" +
                        " id INT AUTO_INCREMENT PRIMARY KEY," +
                        " fecha DATETIME DEFAULT CURRENT_TIMESTAMP," +
                        " total DECIMAL(10, 2) NOT NULL," +
                        " metodo_pago ENUM('EFECTIVO', 'TARJETA', 'TRANSFERENCIA') NOT NULL," +
                        " empleado_id INT," +
                        " FOREIGN KEY (empleado_id) REFERENCES empleados(id))",

                "CREATE TABLE IF NOT EXISTS detalles_ventas (" +
                        " id INT AUTO_INCREMENT PRIMARY KEY," +
                        " venta_id INT," +
                        " producto_id INT," +
                        " cantidad INT NOT NULL," +
                        " subtotal DECIMAL(10, 2) NOT NULL," +
                        " FOREIGN KEY (venta_id) REFERENCES ventas(id)," +
                        " FOREIGN KEY (producto_id) REFERENCES productos(id))",

                "CREATE TABLE IF NOT EXISTS proveedores (" +
                        " id INT AUTO_INCREMENT PRIMARY KEY," +
                        " nombre VARCHAR(100) NOT NULL," +
                        " ruc VARCHAR(13) NOT NULL UNIQUE," +
                        " telefono VARCHAR(15)," +
                        " email VARCHAR(100)," +
                        " direccion VARCHAR(255)," +
                        " productos_suministrados TEXT)",

                "CREATE TABLE IF NOT EXISTS usuarios (" +
                        " id INT AUTO_INCREMENT PRIMARY KEY," +
                        " nombre VARCHAR(100) NOT NULL," +
                        " contraseña VARCHAR(255) NOT NULL," +
                        " rol ENUM('ADMIN', 'GERENTE', 'VENDEDOR') NOT NULL," +
                        " empleado_id INT," +
                        " FOREIGN KEY (empleado_id) REFERENCES empleados(id))"
        };

        for (String sql : sentenciasSQL) {
            stmt.execute(sql);
        }
    }

    private static void insertarDatosIniciales(Statement stmt) throws SQLException {
        stmt.executeUpdate("""
            INSERT INTO empleados (nombre, rfc, telefono, email, rol, sucursal)
            SELECT 'Juan Pérez', 'JUAP123456789', '555-1234', 'juan@example.com', 'ADMIN', 'Sucursal 1'
            FROM DUAL
            WHERE NOT EXISTS (SELECT 1 FROM empleados WHERE rfc = 'JUAP123456789');
        """);

        stmt.executeUpdate("""
            INSERT INTO usuarios (nombre, contraseña, rol, empleado_id)
            SELECT 'admin', 'admin123', 'ADMIN', id FROM empleados
            WHERE rfc = 'JUAP123456789'
            AND NOT EXISTS (SELECT 1 FROM usuarios WHERE nombre = 'admin');
        """);
    }
}
