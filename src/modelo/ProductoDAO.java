package modelo;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ProductoDAO {
    private Connection conexion;

    public ProductoDAO(Connection conexion) {
        this.conexion = conexion;
    }

    public List<Producto> obtenerTodosProductos() throws SQLException {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT * FROM productos";
        
        try (PreparedStatement stmt = conexion.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Producto producto = new Producto(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getDouble("precio"),
                    rs.getDouble("costo"),
                    rs.getInt("stock"),
                    rs.getDate("fecha_caducidad")
                );
                productos.add(producto);
            }
        }
        return productos;
    }

    public Producto obtenerProductoPorId(int id) throws SQLException {
        String sql = "SELECT * FROM productos WHERE id = ?";
        Producto producto = null;
        
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    producto = new Producto(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getDouble("precio"),
                        rs.getDouble("costo"),
                        rs.getInt("stock"),
                        rs.getDate("fecha_caducidad")
                    );
                }
            }
        }
        return producto;
    }

    public boolean agregarProducto(Producto producto) throws SQLException {
        String sql = "INSERT INTO productos (nombre, precio, costo, stock, fecha_caducidad) VALUES (?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, producto.getNombre());
            stmt.setDouble(2, producto.getPrecio());
            stmt.setDouble(3, producto.getCosto());
            stmt.setInt(4, producto.getStock());
            stmt.setDate(5, new java.sql.Date(producto.getFechaCaducidad().getTime()));
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                return false;
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    producto.setId(generatedKeys.getInt(1));
                }
            }
            return true;
        }
    }

    public boolean actualizarProducto(Producto producto) throws SQLException {
        String sql = "UPDATE productos SET nombre = ?, precio = ?, costo = ?, stock = ?, fecha_caducidad = ? WHERE id = ?";
        
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, producto.getNombre());
            stmt.setDouble(2, producto.getPrecio());
            stmt.setDouble(3, producto.getCosto());
            stmt.setInt(4, producto.getStock());
            stmt.setDate(5, new java.sql.Date(producto.getFechaCaducidad().getTime()));
            stmt.setInt(6, producto.getId());
            
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean eliminarProducto(int id) throws SQLException {
        String sql = "DELETE FROM productos WHERE id = ?";
        
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    public List<Producto> buscarProductos(String criterio) throws SQLException {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT * FROM productos WHERE nombre LIKE ? OR id = ?";
        
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, "%" + criterio + "%");
            
            /** Intenta convertir el criterio a número para búsqueda por ID*/
            try {
                int id = Integer.parseInt(criterio);
                stmt.setInt(2, id);
            } catch (NumberFormatException e) {
                stmt.setInt(2, -1); /** Valor que no coincidirá con ningún ID*/
            }
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Producto producto = new Producto(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getDouble("precio"),
                        rs.getDouble("costo"),
                        rs.getInt("stock"),
                        rs.getDate("fecha_caducidad")
                    );
                    productos.add(producto);
                }
            }
        }
        return productos;
    }
}
