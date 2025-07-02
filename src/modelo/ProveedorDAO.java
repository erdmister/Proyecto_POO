package src.modelo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProveedorDAO {
    private Connection conexion;

    public ProveedorDAO(Connection conexion) {
        this.conexion = conexion;
    }

    public List<Proveedor> obtenerTodosProveedores() throws SQLException {
        List<Proveedor> proveedores = new ArrayList<>();
        String sql = "SELECT id, nombre, rfc, telefono, email, direccion FROM proveedores";
        
        try (PreparedStatement stmt = conexion.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Proveedor proveedor = new Proveedor(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("rfc"),
                    rs.getString("telefono"),
                    rs.getString("email"),
                    rs.getString("direccion"),
                    new ArrayList<>() // Inicializa la lista vacía
                );
                proveedores.add(proveedor);
            }
        }
        return proveedores;
    }

    public Proveedor obtenerProveedorPorId(int id) throws SQLException {
        String sql = "SELECT id, nombre, rfc, telefono, email, direccion FROM proveedores WHERE id = ?";
        Proveedor proveedor = null;
        
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    proveedor = new Proveedor(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("rfc"),
                        rs.getString("telefono"),
                        rs.getString("email"),
                        rs.getString("direccion"),
                        new ArrayList<>() // Inicializa la lista vacía
                    );
                }
            }
        }
        return proveedor;
    }

    public boolean agregarProveedor(Proveedor proveedor) throws SQLException {
        String sql = "INSERT INTO proveedores (nombre, rfc, telefono, email, direccion) VALUES (?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, proveedor.getNombre());
            stmt.setString(2, proveedor.getRfc());
            stmt.setString(3, proveedor.getTelefono());
            stmt.setString(4, proveedor.getEmail());
            stmt.setString(5, proveedor.getDireccion());
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas == 0) {
                return false;
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    proveedor.setId(generatedKeys.getInt(1));
                }
            }
            return true;
        }
    }

    public boolean actualizarProveedor(Proveedor proveedor) throws SQLException {
        String sql = "UPDATE proveedores SET nombre = ?, rfc = ?, telefono = ?, email = ?, direccion = ? WHERE id = ?";
        
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, proveedor.getNombre());
            stmt.setString(2, proveedor.getRfc());
            stmt.setString(3, proveedor.getTelefono());
            stmt.setString(4, proveedor.getEmail());
            stmt.setString(5, proveedor.getDireccion());
            stmt.setInt(6, proveedor.getId());
            
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean eliminarProveedor(int id) throws SQLException {
        String sql = "DELETE FROM proveedores WHERE id = ?";
        
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    public List<Proveedor> buscarProveedores(String criterio) throws SQLException {
        List<Proveedor> proveedores = new ArrayList<>();
        String sql = "SELECT id, nombre, rfc, telefono, email, direccion FROM proveedores " +
                     "WHERE nombre LIKE ? OR rfc LIKE ? OR telefono LIKE ? OR email LIKE ?";
        
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            String likePattern = "%" + criterio + "%";
            stmt.setString(1, likePattern);
            stmt.setString(2, likePattern);
            stmt.setString(3, likePattern);
            stmt.setString(4, likePattern);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Proveedor proveedor = new Proveedor(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("rfc"),
                        rs.getString("telefono"),
                        rs.getString("email"),
                        rs.getString("direccion"),
                        new ArrayList<>() // Inicializa la lista vacía
                    );
                    proveedores.add(proveedor);
                }
            }
        }
        return proveedores;
    }
}
